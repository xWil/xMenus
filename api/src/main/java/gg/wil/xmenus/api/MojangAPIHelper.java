package gg.wil.xmenus.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A helper class for Mojang's API
 */
public class MojangAPIHelper {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Map<String, String> UUID_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, JsonObject> PROFILE_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, PlayerProfile> PLAYER_CACHE = new ConcurrentHashMap<>();

    /**
     * Checks if a player's UUID is cached
     * @param name The name of the player
     * @return True if the UUID is cached, false otherwise
     */
    public static boolean isUUIDCached(String name) {
        return UUID_CACHE.containsKey(name);
    }

    /**
     * Checks if a player's profile is cached
     * @param name The name of the player
     * @return True if the profile is cached, false otherwise
     */
    public static boolean isProfileCached(String name) {
        String rawUUID = UUID_CACHE.get(name);
        if (rawUUID == null) return false;
        return PROFILE_CACHE.containsKey(rawUUID);
    }

    /**
     * Checks if a player's skin profile is cached
     * @param name The name of the player
     * @return True if the profile is cached, false otherwise
     */
    public static boolean isPlayerCached(String name) {
        return PLAYER_CACHE.containsKey(name);
    }

    /**
     * Gets the raw UUID of a player from the Mojang API
     * A raw UUID is a UUID without dashes
     *
     * @param name The name of the player
     * @return The raw UUID of the player, or null if the player doesn't exist
     */
    public static String getRawUUID(String name) {
        String cached = UUID_CACHE.get(name);
        if (cached != null) return cached;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name))
                    .build();

            HttpResponse<String> uuidResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (uuidResponse.statusCode() != 200) return null;

            JsonObject uuidJson = JsonParser.parseString(uuidResponse.body()).getAsJsonObject();
            String uuid = uuidJson.get("id").getAsString();
            UUID_CACHE.put(name, uuid);
            return uuid;
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * Gets the UUID of a player from the Mojang API
     *
     * @param name The name of the player
     * @return The UUID of the player, or null if the player doesn't exist
     */
    public static UUID getUUID(String name) {
        String uuid = getRawUUID(name);
        if (uuid == null) return null;

        String sb = uuid.substring(0, 8) + "-" +
                uuid.substring(8, 12) + "-" +
                uuid.substring(12, 16) + "-" +
                uuid.substring(16, 20) + "-" +
                uuid.substring(20);
        return UUID.fromString(sb);
    }

    /**
     * Gets the profile of a player from the Mojang API
     * @param rawUUID The raw UUID of the player
     * @return A JsonObject representing the player's profile, or null if the player doesn't exist
     */
    public static JsonObject getProfile(String rawUUID) {
        try {
            JsonObject cached = PROFILE_CACHE.get(rawUUID);
            if (cached != null) return cached;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + rawUUID))
                    .build();

            HttpResponse<String> profileResponse = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (profileResponse.statusCode() != 200) return null;

            JsonObject profileJson = JsonParser.parseString(profileResponse.body()).getAsJsonObject();
            PROFILE_CACHE.put(rawUUID, profileJson);
            return profileJson;
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * Gets the profile of a player from the Mojang API
     * @param uuid The UUID of the player
     * @return A JsonObject representing the player's profile, or null if the player doesn't exist
     */
    public static JsonObject getProfile(UUID uuid) {
        return getProfile(uuid.toString().replace("-", ""));
    }

    /**
     * Gets a PlayerProfile from a player's name using the Mojang API
     * The {@link PlayerProfile} will have a skin set to the player's
     * skin and a random UUID
     * @param name The name of the player
     * @return A PlayerProfile representing the player's skin, or null if the player doesn't exist
     */
    public static PlayerProfile getPlayerProfile(String name) {
        PlayerProfile cached = PLAYER_CACHE.get(name);
        if (cached != null) return cached;

        String uuid = getRawUUID(name);
        if (uuid == null) return null;

        JsonObject profile = getProfile(uuid);
        if (profile == null) return null;

        try {
            JsonObject properties = profile.getAsJsonArray("properties").get(0).getAsJsonObject();
            String base64 = new String(Base64.getDecoder().decode(properties.get("value").getAsString()));
            JsonObject textureJson = JsonParser.parseString(base64).getAsJsonObject();
            String skinURL = textureJson.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();

            PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID(), name);
            PlayerTextures textures = playerProfile.getTextures();
            textures.setSkin(URI.create(skinURL).toURL());
            playerProfile.setTextures(textures);

            PLAYER_CACHE.put(name, playerProfile);
            return playerProfile;
        } catch (Exception ignored) {}
        return null;
    }
}
