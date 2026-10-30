package com.aether.client.modules.api;

import com.aether.client.events.Render2DEvent;
import com.aether.client.events.Render3DEvent;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Central registry and dispatcher for all Aether Client modules.
 */
public class ModuleManager {
    private final List<Module> modules = new CopyOnWriteArrayList<>();
    private final Map<Class<? extends Module>, Module> moduleClassMap = new HashMap<>();
    private final Map<String, Module> moduleNameMap = new HashMap<>();

    public void register(Module module) {
        if (module == null) return;
        if (moduleNameMap.containsKey(module.getName().toLowerCase(Locale.ROOT))) {
            return;
        }

        modules.add(module);
        moduleClassMap.put(module.getClass(), module);
        moduleNameMap.put(module.getName().toLowerCase(Locale.ROOT), module);
    }

    public void registerAll(Module... modulesToRegister) {
        for (Module m : modulesToRegister) {
            register(m);
        }
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream()
                .filter(m -> m.getCategory() == category)
                .collect(Collectors.toList());
    }

    public List<Module> getEnabledModules() {
        return modules.stream()
                .filter(Module::isEnabled)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) moduleClassMap.get(clazz);
    }

    public Module getModuleByName(String name) {
        if (name == null) return null;
        return moduleNameMap.get(name.toLowerCase(Locale.ROOT));
    }

    public List<Module> search(String query) {
        if (query == null || query.isBlank()) {
            return new ArrayList<>(modules);
        }
        String lower = query.trim().toLowerCase(Locale.ROOT);
        return modules.stream()
                .filter(m -> m.getName().toLowerCase(Locale.ROOT).contains(lower)
                        || m.getDescription().toLowerCase(Locale.ROOT).contains(lower)
                        || m.getCategory().getDisplayName().toLowerCase(Locale.ROOT).contains(lower))
                .collect(Collectors.toList());
    }

    public void onTick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick();
            }
        }
    }

    public void onRender2D(Render2DEvent event) {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onRender2D(event);
            }
        }
    }

    public void onRender3D(Render3DEvent event) {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onRender3D(event);
            }
        }
    }

    public void onKey(int key) {
        if (key <= 0) return;
        for (Module module : modules) {
            if (module.getKey() == key) {
                module.toggle();
            }
        }
    }

    public void enableDefaults() {
        for (Module module : modules) {
            if (module.isDefaultEnabled()) {
                module.setEnabled(true);
            }
        }
    }

    public void disableAll() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.setEnabled(false);
            }
        }
    }

    public int getModuleCount() {
        return modules.size();
    }
}
