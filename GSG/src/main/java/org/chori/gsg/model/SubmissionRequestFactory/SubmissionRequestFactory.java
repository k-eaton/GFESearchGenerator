package org.chori.gsg.model.submissionRequestFactory;

import org.chori.gsg.model.submissionRequests.*;

public class SubmissionRequestFactory {

	private static final SubmissionRequestFactory instance = new SubmissionRequestFactory();
	
	private SubmissionRequestFactory() { }

	public static SubmissionRequestFactory getSubmissionRequestFactoryInstance() {
		return instance;
	}
	
	public static SubmissionRequest assembleSubmissionRequest(String lociType, String whichTab) {
		SubmissionRequest submissionRequest = null;

		switch (lociType) {
		case "HLA":
			switch (whichTab) {
			case "GFE":
				submissionRequest = new HlaGfeSubmissionRequest();
				break;
			case "NAME":
				submissionRequest = new HlaNameSubmissionRequest();
				break;
			case "FEATURE":
				submissionRequest = new HlaFeatureSubmissionRequest();
				break;
			default:
				System.out.println("HLA Switch, Submission Request Factory: haven't added that tab yet");
				break;
			}
			break;

		case "KIR":
			switch (whichTab) {
			case "GFE":
				submissionRequest = new KirGfeSubmissionRequest();
				break;
			case "NAME":
				submissionRequest = new KirNameSubmissionRequest();
				break;
			case "FEATURE":
				submissionRequest = new KirFeatureSubmissionRequest();
				break;
			default:
				System.out.println("KIR Switch, Submission Request Factory: haven't added that tab yet");
				break;
			}

		break;

		default:
			System.out.println("Gene selection switch, Submission Request Factory: haven't added those genes yet");
			break;
		}

		return submissionRequest;
	}
}