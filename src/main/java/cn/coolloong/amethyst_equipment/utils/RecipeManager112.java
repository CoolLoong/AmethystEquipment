package cn.coolloong.amethyst_equipment.utils;

import cn.coolloong.amethyst_equipment.AmethystEquipment;
import cn.nukkit.inventory.Recipe;
import cn.nukkit.inventory.ShapedRecipe;
import cn.nukkit.item.Item;
import com.google.gson.Gson;

import java.io.*;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RecipeManager112 {
    private static final Gson PARSER = new Gson();

    public static void registerRecipeToServer(AmethystEquipment mainClass, String recipe_path) {
        try (var jarFile = new JarFile(mainClass.getFile())) {
            var serverRecipeManager = mainClass.getServer().getCraftingManager();
            Enumeration<JarEntry> jarEntrys = jarFile.entries();
            while (jarEntrys.hasMoreElements()) {
                JarEntry entry = jarEntrys.nextElement();
                String name = entry.getName();
                if (name.startsWith(recipe_path) && !entry.isDirectory()) {
                    // 开始读取文件内容
                    InputStream inputStream = mainClass.getClass().getClassLoader().getResourceAsStream(name);
                    assert inputStream != null;
                    var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    var recipe = parseAddonRecipe(PARSER.fromJson(inputStreamReader, Map.class));
                    if (recipe != null) {
                        serverRecipeManager.registerRecipe(recipe);
                    }
                }
            }
            serverRecipeManager.rebuildPacket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Recipe parseAddonRecipe(Map<?, ?> recipeMap) {
        if (recipeMap.get("format_version").equals("1.12") && recipeMap.containsKey("minecraft:recipe_shaped")) {
            var shape = (Map<?, ?>) recipeMap.get("minecraft:recipe_shaped");
            var identifier = (String) ((Map<?, ?>) shape.get("description")).get("identifier");
            var tags = (String) ((List<?>) shape.get("tags")).get(0);
            var pattern = ((List<?>) shape.get("pattern")).stream().map(a -> (String) a).toList().toArray(new String[]{});
            var ingredients = ((Map<?, ?>) shape.get("key")).entrySet().stream().map(e -> {
                var key = ((String) e.getKey()).toCharArray()[0];
                var value = (Map<?, ?>) e.getValue();
                return Map.entry(key, Item.fromString((String) value.get("item")));
            }).collect(HashMap<Character, Item>::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
            var output = Item.fromString((String) ((Map<?, ?>) shape.get("result")).get("item"));
            return new ShapedRecipe(identifier, 1, output, pattern, ingredients, List.of());
        } else {
            AmethystEquipment.log.error("Recipe file must be in version 1.12 format!");
            return null;
        }
    }
}
