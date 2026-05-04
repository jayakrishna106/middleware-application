import com.middleware.batch_service.BatchService;
import com.middleware.connector_service.ConnectorService;
import com.middleware.custom_logic_service.CustomLogicService;
import com.middleware.distributor_service.DistributorService;
import com.middleware.ops_service.OpsService;
import com.middleware.platform_common.model.MessageEnvelope;
import com.middleware.transformation_service.TransformationService;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class ScenarioTest {
    public static void main(String[] args) {
        MessageEnvelope m = new MessageEnvelope("c1", "a,b,c", Map.of("type", "kafka"));
        ConnectorService connector = new ConnectorService();
        connector.receive(m);
        assert connector.pollIngress() != null;
        connector.send(m);
        assert connector.pollEgress() != null;

        DistributorService distributor = new DistributorService();
        List<MessageEnvelope> split = distributor.splitByComma(m);
        assert split.size() == 3;
        assert "KAFKA_CHANNEL".equals(distributor.route(m));
        assert "JMS_CHANNEL".equals(distributor.route(new MessageEnvelope("c2", "x", Map.of("type", "JMS"))));
        assert "DLQ_CHANNEL".equals(distributor.route(new MessageEnvelope("c3", "x", Map.of())));
        assert "a,b,c".equals(distributor.join(split).getPayload());

        TransformationService tf = new TransformationService();
        String json = tf.xmlToJson("<root><name>alice</name></root>");
        assert "{\"name\":\"alice\"}".equals(json);
        assert "<root><name>alice</name></root>".equals(tf.jsonToXml("{\"name\":\"alice\"}"));
        assert "Hi Bob".equals(tf.applyMustache("Hi {{name}}", "name", "Bob"));
        assert "Hi Bob".equals(tf.applyFtl("Hi ${name}", "name", "Bob"));
        boolean thrown = false;
        try { tf.jsonToXml("invalid"); } catch (IllegalArgumentException e) { thrown = true; }
        assert thrown;

        BatchService batch = new BatchService();
        assert batch.chunk(List.of(1,2,3,4,5), 2).size() == 3;
        thrown = false;
        try { batch.chunk(List.of(1), 0); } catch (IllegalArgumentException e) { thrown = true; }
        assert thrown;

        CustomLogicService custom = new CustomLogicService();
        assert "A,B,C".equals(custom.apply(m, s -> s.toUpperCase()).getPayload());

        OpsService ops = new OpsService();
        ops.audit(m);
        assert ops.manualReprocessByCorrelation("c1").size() == 1;
        assert ops.purgeOlderThan(Instant.now().plusSeconds(1)).size() == 1;

        System.out.println("All middleware scenario tests passed");
    }
}
