import static org.junit.Assert.*;
import org.junit.Test;

import org.chori.gsg.model.SubmissionRequestFactory.*;
import org.chori.gsg.model.SubmissionRequests.*;

public class SetupTest {
 

	SubmissionRequestFactoryInstance submissionRequestFactoryInstance = SubmissionRequestFactoryInstance.getInstance();
	SubmissionRequestFactory submissionRequestFactory = submissionRequestFactoryInstance.factory;
	HlaGfeSubmissionRequest hlaGfeSubReq = new HlaGfeSubmissionRequest();
	HlaNameSubmissionRequest hlaNameSubReq = new HlaNameSubmissionRequest();
	HlaFeatureSubmissionRequest hlaFeatureSubReq = new HlaFeatureSubmissionRequest();


	@Test
	public void testSubmissionRequestFactory() {
		assertSame("The submission request objects do not match, HLA, GFE", hlaGfeSubReq.getClass(),
						submissionRequestFactory.assembleSubmissionRequest("HLA", "GFE").getClass());
		assertSame("The submission request objects do not match, HLA, NAME", hlaNameSubReq.getClass(),
						submissionRequestFactory.assembleSubmissionRequest("HLA", "NAME").getClass());
		assertSame("The submission request objects do not match, HLA, FEATURE", hlaFeatureSubReq.getClass(),
						submissionRequestFactory.assembleSubmissionRequest("HLA", "FEATURE").getClass());
		assertSame("The submission request failures do not match, HLA, ERROR", null,
						submissionRequestFactory.assembleSubmissionRequest("HLA", "ERROR"));
		assertSame("The submission request errors do not match, KIR, ERROR", null,
						submissionRequestFactory.assembleSubmissionRequest("KIR", "ERROR"));
		assertSame("The submission request errors do not match, ABO, FEATURE", null,
						submissionRequestFactory.assembleSubmissionRequest("ABO", "FEATURE"));
	}

	@Test
	public void canSubmissionRequestSeeDataSources() {
		assertEquals("http://neo4j.b12x.org", hlaGfeSubReq.dataSource);
		assertEquals("http://neo4j.b12x.org", hlaNameSubReq.dataSource);
		assertEquals("http://neo4j.b12x.org", hlaFeatureSubReq.dataSource);
		
	}
}