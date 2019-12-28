package language;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LanguageAssetLoader {

  private static final String ASSET_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.14.4/assets/minecraft/lang/";
  private final LanguageAPI api;

  private final HashMap<String, String> language;

  private final File assetFolder;

  public LanguageAssetLoader(final LanguageAPI api) {
    this.api = api;
    language = new HashMap<>();

    assetFolder = new File(api.getHost().getDataFolder(), "assets");
    if (!assetFolder.exists() && assetFolder.mkdirs()) {
      api.getHost().getLogger().info("Created asset folder");
    }
    loadAsset(api.getLanguage());
    if (!language.isEmpty()) {
      api.getHost().getLogger().info("Successfully loaded language file " + api.getLanguage().getLocale() + " with " + language.size() + " entries");
    }
  }

  public String translate(final String path) {
    return language.getOrDefault(path, "[Invalid translation]");
  }

  private void loadAsset(final Language language) {
    if (!new File(assetFolder, language.getLocaleJsonName()).exists()) {
      try {
        downloadAsset(language);
      } catch (final IOException e) {
        api.getHost().getLogger().severe("Failed to download required language asset files");
        e.printStackTrace();
      }
    }
    loadFile(language);
  }

  private void loadFile(final Language language) {
    final Gson gson = new Gson();
    final Type type = new TypeToken<Map<String, String>>() {
    }.getType();

    final File file = new File(assetFolder, language.getLocaleJsonName());
    try (final FileInputStream fs = new FileInputStream(file)) {
      final BufferedReader reader = new BufferedReader(new InputStreamReader(fs, StandardCharsets.UTF_8));
      final Map<String, String> map = gson.fromJson(reader, type);
      this.language.putAll(map);
      reader.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  private void downloadAsset(final Language language) throws IOException {
    final String json;
    final URL url;
    try {
      url = new URL(ASSET_URL + language.getLocaleJsonName());
    } catch (final MalformedURLException e) {
      e.printStackTrace();
      return;
    }

    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setDoOutput(true);
    connection.setInstanceFollowRedirects(false);
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("charset", "utf-8");
    connection.connect();

    try (final InputStream inStream = connection.getInputStream(); final InputStreamReader isr = new InputStreamReader(inStream, StandardCharsets.UTF_8)) {

      int read;
      final StringBuilder builder = new StringBuilder();
      while ((read = isr.read()) != -1) {
        builder.append((char) read);
      }
      json = builder.toString();

      try (final FileWriter file = new FileWriter(new File(assetFolder, language.getLocaleJsonName()))) {
        file.write(json);
      }
    } catch (final IOException ex) {
      ex.printStackTrace();
    } finally {
      connection.disconnect();
    }
  }
}
