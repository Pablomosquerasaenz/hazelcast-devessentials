package essentials.streamProcessing;

import com.hazelcast.function.Functions;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Traverser;
import com.hazelcast.jet.aggregate.AggregateOperation1;
import com.hazelcast.jet.aggregate.AggregateOperations;
import com.hazelcast.jet.contrib.twitter.TwitterSources;
import com.hazelcast.jet.core.AppendableTraverser;
import com.hazelcast.jet.datamodel.Tuple2;
import com.hazelcast.jet.pipeline.*;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import essentials.streamProcessing.support.*;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hazelcast.jet.Util.entry;

public class CryptoSentimentAnalysis {
    private static final String MAP_NAME_JET_RESULTS = "jetResults";

    public static void main(String[] args) {
        Pipeline pipeline = buildPipeline();
        JetInstance jet = Jet.newJetInstance();
        try {
            new CryptoSentimentGui(jet.getMap(MAP_NAME_JET_RESULTS));
            jet.newJob(pipeline).join();
        } finally {
            Jet.shutdownAll();
        }
    }

    private static Pipeline buildPipeline() {
        Pipeline pipeline = Pipeline.create();

        List<String> allCoinMarkers = Stream
                .of(CoinType.values())
                .flatMap(ct -> ct.markers().stream())
                .collect(Collectors.toList());

        StreamStage<String> tweets = pipeline
                .readFrom(
                        TwitterSources.timestampedStream(
                                Util.loadCredentials(),
                                () -> new StatusesFilterEndpoint()
                                        .trackTerms(allCoinMarkers)
                        )
                )
                .withNativeTimestamps(TimeUnit.SECONDS.toMillis(1));

        StreamStageWithKey<Map.Entry<CoinType, Double>, CoinType> tweetsWithSentiment = tweets
                .map(rawTweet -> new JSONObject(rawTweet).getString("text"))
                .flatMap(CryptoSentimentAnalysis::flatMapToRelevant)
                .mapUsingService(sentimentAnalyzerContext(), (analyzer, e1) ->
                        entry(e1.getKey(), analyzer.getSentimentScore(e1.getValue())))
                .filter(e -> !e.getValue().isInfinite() && !e.getValue().isNaN())
                .groupingKey(Functions.entryKey());

        AggregateOperation1<Map.Entry<CoinType, Double>, ?, Tuple2<Double, Long>> avgAndCount = AggregateOperations
                .allOf(AggregateOperations.averagingDouble(Map.Entry::getValue), AggregateOperations.counting());

        tweetsWithSentiment
                .window(WindowDefinition.sliding(WinSize.HALF_MINUTE.durationMillis(), 200))
                .aggregate(avgAndCount)
                .map(windowResult -> entry(Tuple2.tuple2(windowResult.getKey(), WinSize.HALF_MINUTE), windowResult.getValue()))
                .writeTo(Sinks.map(MAP_NAME_JET_RESULTS));

        tweetsWithSentiment
                .window(WindowDefinition.sliding(WinSize.FIVE_MINUTES.durationMillis(), 200))
                .aggregate(avgAndCount)
                .map(windowResult -> entry(Tuple2.tuple2(windowResult.getKey(), WinSize.FIVE_MINUTES), windowResult.getValue()))
                .writeTo(Sinks.map(MAP_NAME_JET_RESULTS));

        return pipeline;
    }

    @Nonnull
    private static ServiceFactory<SentimentAnalyzer, SentimentAnalyzer> sentimentAnalyzerContext() {
        return ServiceFactory
                .withCreateContextFn(jet -> new SentimentAnalyzer())
                .withCreateServiceFn((context, sentimentAnalyzer) -> sentimentAnalyzer);
    }

    private static Traverser<Map.Entry<CoinType, String>> flatMapToRelevant(String text) {
        System.out.println(text);
        text = text.toLowerCase();
        AppendableTraverser<Map.Entry<CoinType, String>> traverser = new AppendableTraverser<>(4);
        for (CoinType ct : CoinType.values()) {
            for (String marker : ct.markers()) {
                if (text.contains(marker.toLowerCase())) {
                    traverser.append(entry(ct, text));
                }
            }
        }
        return traverser;
    }
}
