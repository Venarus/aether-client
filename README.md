# 🌌 Aether Client — Modular Minecraft Client & Modern Click GUI

[![Minecraft 1.21.10](https://img.shields.io/badge/Minecraft-1.21.10-informational?style=for-the-badge&logo=minecraft&logoColor=white&color=008080)](https://minecraft.net/)
[![Minecraft Forge 60.1.18](https://img.shields.io/badge/Minecraft%20Forge-60.1.18-orange?style=for-the-badge&logo=curseforge&logoColor=white)](https://files.minecraftforge.net/)
[![Java 21 LTS](https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://adoptium.net/)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge&logo=gradle&logoColor=white)](https://github.com/Venarus/aether-client)
[![Tests Passing](https://img.shields.io/badge/Tests-27%2F27%20Passed-success?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/)
[![License MIT](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

**Aether Client**, Minecraft Java Edition 1.21.10 için geliştirilmiş, yüksek performanslı, modüler, kararlı ve profesyonel bir istemci modu (experimental client mod) ve modern Click GUI altyapısıdır.

Kullanıcının kendi özel sunucularında (Vanilla, Forge, Paper, Purpur, Spigot, Aternos) ve yerel test dünyalarında deneysel mekanikleri analiz etmek, görsel ve hareket verilerini gerçek zamanlı izlemek ve zengin bir modüler platform deneyimi sunmak amacıyla tasarlanmıştır.

---

## 🌟 Öne Çıkan Özellikler

- **36 Adet Gerçek Oyun İçi Modül:** Sahte (mock/visual-only) modül içermez. Tüm modüller doğrudan Minecraft istemci tick döngüsüne (`ClientTickEvent`), oyuncu yeteneklerine (`Abilities`), varlık oluşturma motoruna (`ClientLevel`) ve girdi sistemine bağlıdır.
- **Dinamik & Otomatik Ölçeklenen Click GUI (`AetherForgeScreen`):** Koyu antrasit (#0D0E12) tasarım dili, ince vurgu kenarlıkları, akıcı animasyonlar, canlı arama ve her çözünürlük ile GUI Scale değerine tam oturan responsive 3 sütunlu yerleşim.
- **Canlı HUD Göstergeleri:** Koordinatlar (Nether karşılığı ve biyom bilgisiyle), Hız Göstergesi (Speedometer - BPS & km/h), CPS Sayacı (Sol/Sağ tık), Keystrokes (W, A, S, D, Boşluk, Shift, Fare), Target HUD (Hedefin anlık can barı ve mesafesi) ve Oturum İstatistikleri.
- **Öncelikli & Thread-Safe Event Bus:** Minecraft ve istemci olaylarını mikrosaniyelik gecikmeyle yöneten, öncelik sıralı (`HIGHEST` ➔ `LOWEST`) ve `Cancellable` destekli olay dağıtıcısı.
- **JSON Tabanlı Profil Yönetimi:** Çoklu profil desteği (`default`, `combat`, `test`), kilitlenmelere karşı dayanıklı atomik dosya yazma (`.tmp` ➔ replace) ve bozuk JSON durumunda çökmeyi önleyen otomatik yedekleme & varsayılana dönüş koruması.
- **Dinamik Kısayol (Keybind) ve Çakışma Tespiti:** GLFW tabanlı tuş yöneticisi, Click GUI içerisinden tek tıkla canlı tuş atama ve çakışan kısayolları otomatik tespit edip kullanıcıyı uyarma mekanizması.

---

## 🎮 Hızlı Başlangıç & Kontroller

| Tuş | Eylem |
| :--- | :--- |
| **`Right Shift` (Sağ Shift)** | **Aether Click GUI** arayüzünü açar ve kapatır |
| **`ESC`** | Arayüzü veya açık arama odaklarını kapatır |
| **`Sol Tık (LMB)`** | Modülleri açar/kapatır (`[ON]` / `[OFF]`) ve ayarlarını gösterir |
| **`Fare Tekerleği`** | Modül ve ayar listelerini pürüzsüzce kaydırır |
| **`Kısayol Butonu`** | Seçili modüle yeni klavye kısayolu atar (`ESC` ile temizler) |

---

## 📂 Proje Mimarisi

```
src/main/java/com/aether/client/
├── AetherMod.java                  # Forge Mod ana yaşam döngüsü (@Mod("aetherclient"))
├── core/
│   ├── AetherClient.java           # Singleton istemci yöneticisi ve alt sistem koordinatörü
│   ├── AetherClientTicker.java     # Canlı oyun içi hile ve tick yürütücüsü (Fullbright, Sprint, Fly, ESP)
│   ├── ForgeClientEvents.java      # Forge EventBus köprüsü (Input, Render, Tick)
│   └── AetherConstants.java        # Sabitler, renk paleti ve sürüm tanımları
├── events/
│   ├── EventBus.java               # Yüksek performanslı olay dağıtıcı motor
│   ├── Subscribe.java              # @Subscribe olay dinleyici anotasyonu
│   ├── EventPriority.java          # Öncelik hiyerarşisi
│   ├── CancellableEvent.java       # İptal edilebilir olay temel sınıfı
│   ├── ClientTickEvent.java        # 20 tick/sn istemci oyun döngüsü kancası
│   ├── Render2DEvent.java          # 2D HUD çizim kancası
│   ├── Render3DEvent.java          # 3D dünya render kancası
│   └── KeyEvent.java               # GLFW tuş yakalama olayı
├── modules/
│   ├── api/
│   │   ├── Module.java             # Modül soyut taban sınıfı
│   │   ├── ModuleManager.java      # Modül listeleme, arama ve güvenli yürütme yöneticisi
│   │   ├── ModuleRegistry.java     # 36 modülün merkezi kayıt defteri
│   │   └── Category.java           # COMBAT, MOVEMENT, VISUALS, PLAYER, WORLD, UTILITY, RENDER, SETTINGS
│   ├── visuals/                    # Fullbright, EntityESP, Tracers, XRay, StorageESP, HealthESP, NameTags, Chams
│   ├── movement/                   # Sprint, Speedometer, Fly, MotionTracker, ElytraHUD, JumpDiagnostics
│   ├── combat/                     # TargetHUD, CPSCounter, ReachDisplay, AimTrainer, CombatLog
│   ├── world/                      # ChestFinder, SpawnerFinder, CaveMapper, ChunkVisualizer, WaypointManager
│   ├── player/                     # Coordinates, ArmorStatus, TotemCounter, PotionEffects, GamemodeManager, InventoryHUD
│   ├── utility/                    # Keystrokes, SessionStatistics, AutoReconnect, InventoryCleaner, ScreenshotManager
│   └── settings/                   # BooleanSetting, NumberSetting, ModeSetting, ColorSetting, KeybindSetting, ClientTheme
├── gui/
│   ├── screens/
│   │   ├── AetherForgeScreen.java  # Native Forge Screen türevi, responsive 3 sütunlu arayüz
│   │   └── ClickGuiScreen.java     # Arayüz veri modeli ve durum yöneticisi
│   ├── HUDManager.java             # Canlı oyun içi HUD çizim yöneticisi
│   ├── components/                 # Modül kartları, ayar bileşenleri, arama çubuğu
│   └── animations/                 # Easing eğrileri ve yumuşak geçiş interpolasyonları
├── config/
│   └── ConfigManager.java          # JSON profil kaydetme/yükleme & atomik kurtarma
├── keybinds/
│   └── KeybindManager.java         # Tuş eşleme ve çakışma tespit sistemi
└── networking/
    └── PermissionDashboard.java    # Sunucu türü, uçuş yetkisi ve ping izleyici
```

---

## 📋 Modül Kataloğu

| Kategori | Modül | Açıklama | Minecraft Etkisi |
| :--- | :--- | :--- | :--- |
| **Visuals** | `Fullbright` | Karanlık mağaraları ve geceleri tam aydınlatır | `MobEffects.NIGHT_VISION` ile tam ortam ışığı |
| **Visuals** | `EntityESP` | Canlıları duvarların arkasından görünür yapar | `Entity.setGlowingTag(true)` |
| **Visuals** | `Tracers` | Hedef varlıklara kılavuz çizgiler çizer | 3B mesafe ve yön hesabı |
| **Visuals** | `XRay` | Yüklenmiş madenleri ve cevherleri filtreler | Blok tarama motoru |
| **Visuals** | `StorageESP`| Sandık, fıçı ve shulker kutularını işaretler | Kutu tarayıcısı |
| **Visuals** | `HealthESP` | Varlıkların can çubuklarını gösterir | Gerçek varlık can oranı |
| **Visuals** | `NameTags`  | Duvardan net okunan etiketler | İsim ve mesafe gösterimi |
| **Visuals** | `Chams`     | Varlık silüetlerine özel renk efekti verir | Parlak dış hat vurgusu |
| **Movement**| `Sprint`    | İleri yürürken otomatik depar atar | `player.setSprinting(true)` |
| **Movement**| `Fly`       | Her oyun modunda serbestçe uçmayı sağlar | `player.getAbilities().flying` |
| **Movement**| `Speedometer`| Anlık hareket hızını ölçer (m/s & km/h) | Delta vektör fiziği `(dX, dZ) * 20` |
| **Movement**| `MotionTracker`| 3B hız vektörleri (X/Y/Z) ve ivmelenme | Anlık hız grafiği |
| **Movement**| `ElytraHUD` | Süzülme hızı, açı ve dayanıklılık bilgisi | Uçuş HUD'ı |
| **Movement**| `JumpDiagnostics`| Zıplama yüksekliği ve havada kalma süresi | Zıplama telemetrisi |
| **Combat**  | `TargetHUD` | Nişangahtaki varlığın can barı ve mesafesi | `crosshairPickEntity` |
| **Combat**  | `CPSCounter`| Canlı Sol/Sağ tıklama hızı (Tıklama/Saniye) | Girdi dinleyicisi |
| **Combat**  | `ReachDisplay`| Vuruş mesafesi analizi ve menzil kaydı | Saldırı mesafesi ölçer |
| **Combat**  | `CombatLog` | Alınan ve verilen hasar dökümü | Savaş telemetrisi |
| **Player**  | `Coordinates`| Oyuncunun gerçek X/Y/Z, Nether ve Biyom bilgisi | Dünya koordinatları |
| **Player**  | `ArmorStatus`| Zırh parçaları ve dayanıklılık yüzdeleri | Envanter zırh takibi |
| **Player**  | `TotemCounter`| Envanterdeki Totem of Undying sayısı | Otomatik totem sayımı |
| **Player**  | `PotionEffects`| Aktif iksir etkileri ve kalan süreleri | İksir listesi |
| **Player**  | `InventoryHUD`| Envanter yuvalarını ekranda gösterir | Envanter HUD'ı |
| **Player**  | `GamemodeManager`| Hızlı oyun modu geçişi | Gamemode komut köprüsü |
| **Utility** | `Keystrokes`| WASD, Boşluk, Shift ve fare tuş vuruşları | Canlı tuş göstergesi |
| **Utility** | `SessionStatistics`| Oturum süresi, yürünen yol ve FPS | İstatistik sayacı |
| **Utility** | `AutoReconnect`| Bağlantı koptuğunda otomatik yeniden bağlanır | Sunucu yeniden bağlanma |

---

## 🛠️ Derleme ve Kurulum

### Gereksinimler
- **Java Development Kit (JDK):** Java 21 LTS (Temurin / Adoptium önerilir)
- **Minecraft:** Java Edition 1.21.10
- **Mod Yükleyici:** Minecraft Forge 1.21.10 (Sürüm `60.1.18`)

### Projeyi Sıfırdan Derleme
Terminal veya PowerShell üzerinden proje ana dizininde şu komutu çalıştırın:

```bash
# Windows
.\gradlew.bat clean build --no-daemon

# Linux / macOS
chmod +x gradlew
./gradlew clean build --no-daemon
```

Derleme tamamlandığında doğrulanmış mod JAR dosyası şu konumda oluşturulur:
📁 `build/libs/aether-client-1.0.0.jar`

### Modu Minecraft'a Yükleme
1. `build/libs/aether-client-1.0.0.jar` dosyasını kopyalayın.
2. Minecraft Forge 1.21.10 profilinizin `mods/` klasörüne yapıştırın:
   - Standart Launcher: `%appdata%\.minecraft\mods\`
   - CurseForge: `curseforge\minecraft\Instances\<Profil_Adı>\mods\`
3. Oyunu başlatın ve dünyanıza girin.
4. **Sağ Shift (Right Shift)** tuşuna basarak Aether Client'ı kullanmaya başlayın!

---

## 🧪 Test Paketi

Projede yer alan tüm çekirdek bileşenler, ayar sınıfları, profil yöneticisi ve modül mantıkları JUnit 5 ile test edilmiştir:

```bash
.\gradlew.bat test
```
> **Test Sonucu:** `27 tests completed, 0 failed, 0 skipped` (%100 Başarılı)

---

## 📄 Lisans

Bu proje [MIT Lisansı](LICENSE) kapsamında açık kaynak olarak yayınlanmıştır.
Tüm hakları saklıdır © 2026 **Venarus**.
