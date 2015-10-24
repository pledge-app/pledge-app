package com.pledgeapp.pledge;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pledgeapp.pledge.models.NonProfit;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 *
 * 
 */
public class PledgeClient {
	public static final String PLEDGE_SERVICE_BASE_URL = "http://146.148.84.121";

    private AsyncHttpClient mClient;

	public PledgeClient() {
        // TODO(ageiduschek): Authenticate requests with Google Oauth token.
        mClient = new AsyncHttpClient();
	}

	public void getFeatured(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("/featured");

		RequestParams params = new RequestParams();

		mClient.get(apiUrl, params, handler);
	}

	public void getLocal(AsyncHttpResponseHandler handler) {
		// TODO(nikhilb): Update this endpoint when you actually build "/local"

		String apiUrl = getApiUrl("/featured");

		RequestParams params = new RequestParams();

        mClient.get(apiUrl, params, handler);
	}

	public void search(String query, NonProfit.CategoryInfo category, AsyncHttpResponseHandler handler) {
		search(query, category, 1 /*page*/, handler);
	}

	public void search(String query, NonProfit.CategoryInfo category, int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("/search");

        RequestParams params = new RequestParams();
        params.put("q", query);
		if (category != null) {
			params.put("category", category.searchIndex);
		}
        params.put("page", page);

        mClient.get(apiUrl, params, handler);
	}

    protected String getApiUrl(String path) {
        return PLEDGE_SERVICE_BASE_URL + path;
    }

}