package essentials.introduction;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.config.ProcessingGuarantee;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.test.TestSources;

public class GettingStartedClientServer {
    public static void main(String[] args) {
        Pipeline pipeline = Pipeline.create();

        pipeline.readFrom(TestSources.itemStream(10))
                .withoutTimestamps()
                .filter(event -> event.sequence() % 2 == 0)
                .setName("filter out odd numbers")
                .writeTo(Sinks.logger());

        JobConfig cfg = new JobConfig();
        cfg.setName("getting-started");
        cfg.setProcessingGuarantee(ProcessingGuarantee.EXACTLY_ONCE);

        JetInstance jet = Jet.bootstrappedInstance();

        jet.newJob(pipeline, cfg).join();
    }
}
