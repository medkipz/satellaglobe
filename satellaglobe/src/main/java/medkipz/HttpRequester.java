package medkipz;
import java.net.URI;
import java.net.http.*;

public final class HttpRequester {
	private static final HttpClient client = HttpClient.newHttpClient();

	/**
	 * Fetch data from a REST API endpoint.
	 * @param url The URL to fetch data from
	 * @return The response body as a String
	 */
	public static final String getUrlData(String url) {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.build();

		return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
			.thenApply((HttpResponse<String> response) -> response.body())
			.join();
	}
}