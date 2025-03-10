package com.example.demo.controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoubanScraper {

    // Function to scrape the movie list and perform curl with each IMDb code
    public static List<String> getMovieImdbCodesAndCurl(String imdbId, String douId) {
        List<String> imdbCodes = new ArrayList<>();
        int start = 0;

        while (true) {
            try {
                System.out.println("start:" + start);
                Thread.sleep(1000);
                // Construct URL with current 'start' value
                String url = "https://www.douban.com/doulist/" + douId + "/?start=" + start + "&sort=seq&playable=0&sub_type=";
                System.out.println("url:" + url);
                Document doc = Jsoup.connect(url).get();

                // Select all links that start with "https://movie.douban.com/subject"
                Elements links = doc.select("a[href^=https://movie.douban.com/subject]");

                // If no links are found, exit the loop (end of list)
                if (links.isEmpty()) {
                    break;
                }

                // Iterate over each movie link
                for (org.jsoup.nodes.Element link : links) {
                    String movieUrl = link.absUrl("href");
                    Thread.sleep(1000);
                    Document movieDoc = null;

                    int retry = 0;
                    while (retry < 10) {
                        try {
                            movieDoc = Jsoup.connect(movieUrl).get();
                        } catch (Exception e) {
                            Thread.sleep(1000);
                        }
                        if (movieDoc != null) {
                            break;
                        }
                        retry++;
                    }
                    if (movieDoc == null) {
                        throw new Exception("null");
                    }
                    // Extract IMDb code from page text (e.g., "IMDb: tt1027873")
                    String pageContent = movieDoc.text();
                    Matcher matcher = Pattern.compile("IMDb: (tt\\d+)").matcher(pageContent);
                    while (matcher.find()) {
                        String imdbCode = matcher.group(1);
                        if (imdbCodes.contains(imdbCode)) {
                            continue;// Extract the IMDb code (e.g., tt1027873)
                        }
                        imdbCodes.add(imdbCode);

                        // Perform curl with the IMDb code as listId (constId)
                        executeCurlCommand(imdbCode, imdbId);
                    }
                }

                // Move to the next page by incrementing 'start' by 25
                start += 25;

            } catch (IOException e) {
                e.printStackTrace();
                break;  // Exit if there is an error (e.g., connection issue)
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return imdbCodes;
    }

    // Function to execute the curl command with the extracted IMDb code (imdbCode)
    private static void executeCurlCommand(String imdbCode, String imdbId) {
        String url = "curl 'https://api.graphql.imdb.com/' \\\n" +
                "  -H 'accept: application/graphql+json, application/json' \\\n" +
                "  -H 'accept-language: en,zh;q=0.9,en-US;q=0.8,zh-CN;q=0.7,fr;q=0.6,it;q=0.5' \\\n" +
                "  -H 'content-type: application/json' \\\n" +
                "  -H 'cookie: session-id=130-1748680-7633418; ubid-main=131-4115693-0420961; at-main=Atza|IwEBINGyAPXuujz1atc1OtdMKIZjE2-nlUB1zFdEnCXxkgwnYZPS5rs5FzeUwKJHngwnVP1bPjSR6jnWSNHumJOH04YHg-IxLm-kuHywSsYHnkg6MXM9--5UOIgtUkb7CDZxaDH_jc52xK1R-4Z2XoEW3GDtQ4ovbBB0yDW76jBXNOeOxPlaguTkmlw7Fx7JfqwJKSQR-buEPMnzClN-bblSucYj5YKFXMT32UO2wrIITH2dug; sess-at-main=\"Q7gvVjKE90dr3e1kX2q5/BC357WwJ3Eun7vJGEq8lpM=\"; uu=eyJpZCI6InV1NzliNTQxOTYzMjYwNGQ1ZWJjN2MiLCJwcmVmZXJlbmNlcyI6eyJmaW5kX2luY2x1ZGVfYWR1bHQiOmZhbHNlfSwidWMiOiJ1cjQ1MDE2OTQ2In0=; session-id-time=2082787201l; ci=e30; ad-oo=0; x-main=uoEJ9OwqkrMl4HWxvDC86MaSEvSYyYNFkh2ZiCqFGecppciYt7hZTlx3GQ3hpiAR; session-token=XS4SOYfpAzwGfQuC2kptxzPFXOJbxBCaLsayYx9IEDvwVCg5x/uRFk/J+e4HpKT+xxf5VS1jOoiCUDnxz853Vg2cUumPJvafsf3GQ0nFjbq7ibFFl4KrwL6qKBa1+EzRxo4WaZKd5h2XiS87o1We6RI4WaBxuVcNyl/4ZktE26dSXtB39Dn664Z4cZIX3YILB2CGc7dPPYFzK94AIYBu6s3VydbPjbtwuzhIyuyzBccyrw9UXmTs3YtDsSMxipxHblgSXQnJoX0fXByULwMD9+eML6XjGUNY+5Hee65UTZ54rH3gw00RjKXHVVF3DU/0CR0A30KmmrJiNvajq3uBi9weo668VQjvUHhhelNOFik5kmDUyLJlNQwnkx3+VtIS' \\\n" +
                "  -H 'origin: https://www.imdb.com' \\\n" +
                "  -H 'priority: u=1, i' \\\n" +
                "  -H 'referer: https://www.imdb.com/' \\\n" +
                "  -H 'sec-ch-ua: \"Chromium\";v=\"130\", \"Google Chrome\";v=\"130\", \"Not?A_Brand\";v=\"99\"' \\\n" +
                "  -H 'sec-ch-ua-mobile: ?0' \\\n" +
                "  -H 'sec-ch-ua-platform: \"macOS\"' \\\n" +
                "  -H 'sec-fetch-dest: empty' \\\n" +
                "  -H 'sec-fetch-mode: cors' \\\n" +
                "  -H 'sec-fetch-site: same-site' \\\n" +
                "  -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36' \\\n" +
                "  -H 'x-amzn-sessionid: 130-1748680-7633418' \\\n" +
                "  -H 'x-imdb-client-name: imdb-web-next-localized' \\\n" +
                "  -H 'x-imdb-client-rid: G12FVKFKAX0Z6RTDCQ3H' \\\n" +
                "  -H 'x-imdb-user-country: US' \\\n" +
                "  -H 'x-imdb-user-language: en-US' \\\n" +
                "  -H 'x-imdb-weblab-treatment-overrides: {\"IMDB_NAV_PRO_FLY_OUT_NOV_PROMO_1087903\":\"T1\"}' \\\n" +
                "  --data-raw $'{\"query\":\"mutation AddConstToList($listId: ID\\u0021, $constId: ID\\u0021, $includeListItemMetadata: Boolean\\u0021, $refTagQueryParam: String, $originalTitleText: Boolean) {\\\\n  addItemToList(input: {listId: $listId, item: {itemElementId: $constId}}) {\\\\n    listId\\\\n    modifiedItem {\\\\n      ...EditListItemMetadata\\\\n      listItem @include(if: $includeListItemMetadata) {\\\\n        ... on Title {\\\\n          ...TitleListItemMetadata\\\\n        }\\\\n        ... on Name {\\\\n          ...NameListItemMetadata\\\\n        }\\\\n        ... on Image {\\\\n          ...ImageListItemMetadata\\\\n        }\\\\n        ... on Video {\\\\n          ...VideoListItemMetadata\\\\n        }\\\\n      }\\\\n    }\\\\n  }\\\\n}\\\\n\\\\nfragment EditListItemMetadata on ListItemNode {\\\\n  itemId\\\\n  createdDate\\\\n  absolutePosition\\\\n  description {\\\\n    originalText {\\\\n      markdown\\\\n      plaidHtml(showLineBreak: true)\\\\n      plainText\\\\n    }\\\\n  }\\\\n}\\\\n\\\\nfragment TitleListItemMetadata on Title {\\\\n  ...BaseTitleCard\\\\n  plot {\\\\n    plotText {\\\\n      plainText\\\\n    }\\\\n  }\\\\n  latestTrailer {\\\\n    id\\\\n  }\\\\n  series {\\\\n    series {\\\\n      id\\\\n      originalTitleText {\\\\n        text\\\\n      }\\\\n      releaseYear {\\\\n        endYear\\\\n        year\\\\n      }\\\\n      titleText {\\\\n        text\\\\n      }\\\\n    }\\\\n  }\\\\n}\\\\n\\\\nfragment BaseTitleCard on Title {\\\\n  id\\\\n  titleText {\\\\n    text\\\\n  }\\\\n  titleType {\\\\n    id\\\\n    text\\\\n    canHaveEpisodes\\\\n    displayableProperty {\\\\n      value {\\\\n        plainText\\\\n      }\\\\n    }\\\\n  }\\\\n  originalTitleText {\\\\n    text\\\\n  }\\\\n  primaryImage {\\\\n    id\\\\n    width\\\\n    height\\\\n    url\\\\n    caption {\\\\n      plainText\\\\n    }\\\\n  }\\\\n  releaseYear {\\\\n    year\\\\n    endYear\\\\n  }\\\\n  ratingsSummary {\\\\n    aggregateRating\\\\n    voteCount\\\\n  }\\\\n  runtime {\\\\n    seconds\\\\n  }\\\\n  certificate {\\\\n    rating\\\\n  }\\\\n  canRate {\\\\n    isRatable\\\\n  }\\\\n  titleGenres {\\\\n    genres(limit: 3) {\\\\n      genre {\\\\n        text\\\\n      }\\\\n    }\\\\n  }\\\\n  canHaveEpisodes\\\\n}\\\\n\\\\nfragment NameListItemMetadata on Name {\\\\n  id\\\\n  primaryImage {\\\\n    url\\\\n    caption {\\\\n      plainText\\\\n    }\\\\n    width\\\\n    height\\\\n  }\\\\n  nameText {\\\\n    text\\\\n  }\\\\n  primaryProfessions {\\\\n    category {\\\\n      text\\\\n    }\\\\n  }\\\\n  knownFor(first: 1) {\\\\n    edges {\\\\n      node {\\\\n        summary {\\\\n          yearRange {\\\\n            year\\\\n            endYear\\\\n          }\\\\n        }\\\\n        title {\\\\n          id\\\\n          originalTitleText {\\\\n            text\\\\n          }\\\\n          titleText {\\\\n            text\\\\n          }\\\\n          titleType {\\\\n            canHaveEpisodes\\\\n          }\\\\n        }\\\\n      }\\\\n    }\\\\n  }\\\\n  bio {\\\\n    displayableArticle {\\\\n      body {\\\\n        plaidHtml(\\\\n          queryParams: $refTagQueryParam\\\\n          showOriginalTitleText: $originalTitleText\\\\n        )\\\\n      }\\\\n    }\\\\n  }\\\\n}\\\\n\\\\nfragment ImageListItemMetadata on Image {\\\\n  id\\\\n  url\\\\n  height\\\\n  width\\\\n  caption {\\\\n    plainText\\\\n  }\\\\n  names(limit: 4) {\\\\n    id\\\\n    nameText {\\\\n      text\\\\n    }\\\\n  }\\\\n  titles(limit: 1) {\\\\n    id\\\\n    titleText {\\\\n      text\\\\n    }\\\\n    originalTitleText {\\\\n      text\\\\n    }\\\\n    releaseYear {\\\\n      year\\\\n      endYear\\\\n    }\\\\n  }\\\\n}\\\\n\\\\nfragment VideoListItemMetadata on Video {\\\\n  id\\\\n  thumbnail {\\\\n    url\\\\n    width\\\\n    height\\\\n  }\\\\n  name {\\\\n    value\\\\n    language\\\\n  }\\\\n  description {\\\\n    value\\\\n    language\\\\n  }\\\\n  runtime {\\\\n    unit\\\\n    value\\\\n  }\\\\n  primaryTitle {\\\\n    id\\\\n    originalTitleText {\\\\n      text\\\\n    }\\\\n    titleText {\\\\n      text\\\\n    }\\\\n    titleType {\\\\n      canHaveEpisodes\\\\n    }\\\\n    releaseYear {\\\\n      year\\\\n      endYear\\\\n    }\\\\n  }\\\\n}\",\"operationName\":\"AddConstToList\",\"variables\":{\"listId\":\"%s\",\"constId\":\"%s\",\"includeListItemMetadata\":false,\"refTagQueryParam\":\"tt_ov_ls\",\"originalTitleText\":false}}'";
        try {
            String curlCommand = String.format(
                   url, imdbId, imdbCode);

            System.out.println("curlz:");
            System.out.println(curlCommand);
            // Execute the curl command using ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", curlCommand);
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("Curl executed for IMDb code: " + imdbCode);

            // Capture the output (response) of the curl command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }

            // Print the response
            System.out.println("Response for IMDb Code " + imdbCode + ":");
            System.out.println(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String imdbId = "ls597280780", douId = "41027468";  // Example IMDb list ID (this is the input)
        getMovieImdbCodesAndCurl(imdbId, douId);
    }
}
