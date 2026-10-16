## [1.11.2](https://github.com/monesys/aimal-patches/compare/v1.11.1...v1.11.2) (2026-10-16)


### Bug Fixes

* **scd:** neutralize UnityPlayer.queueDestroy and P0.run pause shutdown ([5eb6ba1](https://github.com/monesys/aimal-patches/commit/5eb6ba1f0df821a4f1d92c842387e71bf376f909))
* **scd:** resolve mutableClassDefByOrNull for UnityPlayer and P0 ([1cc847f](https://github.com/monesys/aimal-patches/commit/1cc847f213791abe2ed94cc474d3ed08b7ebc96d))
* **scd:** use addInstructions for P0.run instruction insertion ([605d940](https://github.com/monesys/aimal-patches/commit/605d9408a0b33674cc6a226ed21785cd0250ebbc))

## [1.11.1](https://github.com/monesys/aimal-patches/compare/v1.11.0...v1.11.1) (2026-10-16)


### Bug Fixes

* **scd:** revert ACTk CodeHashGenerator neutering to prevent async coroutine hangs ([3f01377](https://github.com/monesys/aimal-patches/commit/3f01377447f5660c28015acf39b752b68cfd37c6))

# [1.11.0](https://github.com/monesys/aimal-patches/compare/v1.10.1...v1.11.0) (2026-10-16)


### Features

* **scd:** neuter ACTk CodeHashGenerator and UnityPlayer.kill to prevent anti-tamper aborts ([60237b6](https://github.com/monesys/aimal-patches/commit/60237b6de2a53efbca7ded3bd8c5b1e6cb2ead4c))

## [1.10.1](https://github.com/monesys/aimal-patches/compare/v1.10.0...v1.10.1) (2026-10-16)


### Bug Fixes

* **scd:** remove out-of-range register bytecode from PAD getAssetPackPath to prevent VerifyError ([670abde](https://github.com/monesys/aimal-patches/commit/670abde530855ee25543ea82849e1bafa19c50a3))

# [1.10.0](https://github.com/monesys/aimal-patches/compare/v1.9.0...v1.10.0) (2026-10-16)


### Features

* **scd:** add Play Asset Delivery split source resolver for UnityDataAssetPack ([52bd467](https://github.com/monesys/aimal-patches/commit/52bd46788304becb3a7aa6ac7d2a5fa2e6e0e30e))

# [1.9.0](https://github.com/monesys/aimal-patches/compare/v1.8.0...v1.9.0) (2026-10-16)


### Features

* **scd:** add comprehensive crash killer neutralizer for LicenseContentProvider, LicenseActivity, and handleError ([131c5f4](https://github.com/monesys/aimal-patches/commit/131c5f47696dba63d39ed4b3296a55a0266c267c))

# [1.8.0](https://github.com/monesys/aimal-patches/compare/v1.7.0...v1.8.0) (2026-10-16)


### Features

* **scd:** add comprehensive PairIP license kill and shutdown bypass for non-root environments ([ccfdbce](https://github.com/monesys/aimal-patches/commit/ccfdbce56e2dc0957b7adbabc1543574d8d81199))

# [1.7.0](https://github.com/monesys/aimal-patches/compare/v1.6.0...v1.7.0) (2026-10-16)


### Features

* **scd:** add direct reward callback hooks to AdapterMAX ShowRewardVideo and ShowInterstitialVideo ([ef58182](https://github.com/monesys/aimal-patches/commit/ef58182ca49b616480e1bfe7753ccd99f3f5295b))

# [1.6.0](https://github.com/monesys/aimal-patches/compare/v1.5.1...v1.6.0) (2026-10-16)


### Features

* **scd:** add AyoomiUnity direct callback hooks and AdapterMAX readiness bypass ([bc87096](https://github.com/monesys/aimal-patches/commit/bc87096b654cabadbc2f20328e018c0a8d127c4e))

## [1.5.1](https://github.com/monesys/aimal-patches/compare/v1.5.0...v1.5.1) (2026-10-16)


### Bug Fixes

* **scd:** invert null check branch to properly invoke reward and interstitial callbacks ([749e69a](https://github.com/monesys/aimal-patches/commit/749e69a5ceb2c7b28e8a275b0b18d70e02392486))

# [1.5.0](https://github.com/monesys/aimal-patches/compare/v1.4.1...v1.5.0) (2026-10-16)


### Bug Fixes

* **scd:** resolve merge conflict in AyoomiInstantRewardsPatch ([0ecdaa4](https://github.com/monesys/aimal-patches/commit/0ecdaa41c5d5a807da906826d0c111c9d02d08fc))


### Features

* **scd:** add Ayoomi Instant Rewards with readiness bypass and auto-complete callbacks ([acb35f7](https://github.com/monesys/aimal-patches/commit/acb35f775398003ea81170e2b9272a2178cc9852))

## [1.4.1](https://github.com/monesys/aimal-patches/compare/v1.4.0...v1.4.1) (2026-10-15)


### Bug Fixes

* **scd:** add RewardAdIsReady gate bypass and fix listener null check ([a1e67ea](https://github.com/monesys/aimal-patches/commit/a1e67eade615d82f1c19a30eb05499f7ecfa4794))

# [1.4.0](https://github.com/monesys/aimal-patches/compare/v1.3.1...v1.4.0) (2026-10-15)


### Bug Fixes

* **scd:** resolve merge conflict and fix DSL syntax in AyoomiInstantRewardsPatch ([ca03b7a](https://github.com/monesys/aimal-patches/commit/ca03b7af8409c4c3b0891c714df70dbb3eb52577))


### Features

* **scd:** add Ayoomi Instant Rewards patch for instant rewarded ad rewards ([a789eb9](https://github.com/monesys/aimal-patches/commit/a789eb9f9d44d27c2243a75a5dae4a7be9b1c224))
* **scd:** add Ayoomi Instant Rewards patch for Serpent Chain Defense ([8f5430c](https://github.com/monesys/aimal-patches/commit/8f5430c0a6a5fb81be0a91f902a9132c772717ed))

## [1.3.1](https://github.com/monesys/aimal-patches/compare/v1.3.0...v1.3.1) (2026-10-11)


### Bug Fixes

* **disney:** resolve CheckQuery ResponseAdapter fingerprint for R8-minified builds ([1dc82c5](https://github.com/monesys/aimal-patches/commit/1dc82c57adb1858e65df5fc63a3e6efd0cbc0ee0))

# [1.3.0](https://github.com/monesys/aimal-patches/compare/v1.2.2...v1.3.0) (2026-10-11)


### Features

* **disney:** add direct password login patch to bypass OTP prompt ([c0d0a10](https://github.com/monesys/aimal-patches/commit/c0d0a1056f6f5d7d7289a6f7d2726e06587f79b8))

## [1.2.2](https://github.com/monesys/aimal-patches/compare/v1.2.1...v1.2.2) (2026-09-21)


### Bug Fixes

* hook Disney native skip state directly ([f1297ba](https://github.com/monesys/aimal-patches/commit/f1297baa234ed5d422d1e5f040aa7f9ab625e082))

## [1.2.1](https://github.com/monesys/aimal-patches/compare/v1.2.0...v1.2.1) (2026-09-21)


### Bug Fixes

* match Disney skip listener across invocation forms ([5b2a6d5](https://github.com/monesys/aimal-patches/commit/5b2a6d560457b74dd3aee82843910ad27309e22b))

# [1.2.0](https://github.com/monesys/aimal-patches/compare/v1.1.1...v1.2.0) (2026-09-21)


### Features

* add native Disney+ intro skip ([45bf720](https://github.com/monesys/aimal-patches/commit/45bf720bd349e9c58993f4ad1b403050ef71916c))

## [1.1.1](https://github.com/monesys/aimal-patches/compare/v1.1.0...v1.1.1) (2026-09-21)


### Bug Fixes

* warn against Disney Plus Android TV build ([aec8f80](https://github.com/monesys/aimal-patches/commit/aec8f80d72fa0b18c151dc719bcb2ff4adfd9792))

# [1.1.0](https://github.com/monesys/aimal-patches/compare/v1.0.0...v1.1.0) (2026-09-21)


### Features

* add finer playback speed steps ([8c94250](https://github.com/monesys/aimal-patches/commit/8c9425062fd87d336644a918013a033e1e485fb1))

# 1.0.0 (2026-09-21)


### Bug Fixes

* add the missing Viki side-panel extension class ([e06d8d8](https://github.com/monesys/aimal-patches/commit/e06d8d878e9361ce040226b59d8e1820a94d109d))
* add the patch list generator the release step calls ([50cf8a8](https://github.com/monesys/aimal-patches/commit/50cf8a8750b7e4a2a3d09c8a2179bf0a63793293))
* attach only the current release's .mpp, not the previous one too ([f7dec69](https://github.com/monesys/aimal-patches/commit/f7dec69e9a7e8d85f8e5b8232581d8fb8e7099a4))
* drop the initialize hook that stopped subtitles loading ([27f203b](https://github.com/monesys/aimal-patches/commit/27f203b1bec306fbf37a9dc4e13b207d034d4944))
* make Crunchyroll subtitle settings actually take effect ([9f8683e](https://github.com/monesys/aimal-patches/commit/9f8683e9913b399e95c8980bc81233516ec538f5))
* make the Crunchyroll aspect toggle appear on 3.117.0 ([672cafe](https://github.com/monesys/aimal-patches/commit/672cafe273b6ee8d21cdb10b14f80e77521d707f))
* match Crunchyroll speed table by structure, not moved strings ([04a7d4c](https://github.com/monesys/aimal-patches/commit/04a7d4c2065c3ce7ca6406f69182afb43fe4bc51))
* register the generatePatchesList task used by the release ([32be357](https://github.com/monesys/aimal-patches/commit/32be357cfb2b3438e7234117d6e6d0916ede88ca))
* report whether the Crunchyroll subtitle hook is actually present ([74948b6](https://github.com/monesys/aimal-patches/commit/74948b6db66f8be9434580d6b6edae2971d51d05))
* update readme generator so any-version targets do not crash it ([0ed2c48](https://github.com/monesys/aimal-patches/commit/0ed2c480c05f045fc8a297d9e7d74f64401ea8df))


### Features

* add Viki ad removal and HD unlock patches ([9e779e9](https://github.com/monesys/aimal-patches/commit/9e779e96f456a9d7bc4ffa4a79e8dba376cfe177))
* give the Viki player the whole screen on unfolded foldables ([1188522](https://github.com/monesys/aimal-patches/commit/1188522023c401ede6f6e7efa9db29b454fd92e7))
* improve Disney+ playback controls ([46f6be3](https://github.com/monesys/aimal-patches/commit/46f6be34b02af78d8874bdc7c6a22d9e3c76511a))
* playback speed and aspect ratio for Crunchyroll, HBO Max and Disney+ ([11ba11d](https://github.com/monesys/aimal-patches/commit/11ba11d0136d74410a121a68233226d335f7b508))
* subtitle styling for Crunchyroll ([7f247cb](https://github.com/monesys/aimal-patches/commit/7f247cbb0f9e3b9875b4a256c0dc36efa606e825))
* subtitle styling for HBO Max and Disney+ ([6514870](https://github.com/monesys/aimal-patches/commit/6514870e9b00cb5d0224fe621e0bfbf45452e0ca))

# [1.1.0](https://github.com/hashtagbasit/aimal-patches/compare/v1.0.2...v1.1.0) (2026-09-06)


### Bug Fixes

* add the missing Viki side-panel extension class ([e06d8d8](https://github.com/hashtagbasit/aimal-patches/commit/e06d8d878e9361ce040226b59d8e1820a94d109d))
* attach only the current release's .mpp, not the previous one too ([f7dec69](https://github.com/hashtagbasit/aimal-patches/commit/f7dec69e9a7e8d85f8e5b8232581d8fb8e7099a4))
* drop the initialize hook that stopped subtitles loading ([27f203b](https://github.com/hashtagbasit/aimal-patches/commit/27f203b1bec306fbf37a9dc4e13b207d034d4944))
* make Crunchyroll subtitle settings actually take effect ([9f8683e](https://github.com/hashtagbasit/aimal-patches/commit/9f8683e9913b399e95c8980bc81233516ec538f5))
* report whether the Crunchyroll subtitle hook is actually present ([74948b6](https://github.com/hashtagbasit/aimal-patches/commit/74948b6db66f8be9434580d6b6edae2971d51d05))


### Features

* add Viki ad removal and HD unlock patches ([9e779e9](https://github.com/hashtagbasit/aimal-patches/commit/9e779e96f456a9d7bc4ffa4a79e8dba376cfe177))
* give the Viki player the whole screen on unfolded foldables ([1188522](https://github.com/hashtagbasit/aimal-patches/commit/1188522023c401ede6f6e7efa9db29b454fd92e7))
* subtitle styling for Crunchyroll ([7f247cb](https://github.com/hashtagbasit/aimal-patches/commit/7f247cbb0f9e3b9875b4a256c0dc36efa606e825))
* subtitle styling for HBO Max and Disney+ ([6514870](https://github.com/hashtagbasit/aimal-patches/commit/6514870e9b00cb5d0224fe621e0bfbf45452e0ca))

# [1.1.0-dev.9](https://github.com/hashtagbasit/aimal-patches/compare/v1.1.0-dev.8...v1.1.0-dev.9) (2026-08-25)


### Bug Fixes

* attach only the current release's .mpp, not the previous one too ([f7dec69](https://github.com/hashtagbasit/aimal-patches/commit/f7dec69e9a7e8d85f8e5b8232581d8fb8e7099a4))

# [1.1.0-dev.8](https://github.com/hashtagbasit/aimal-patches/compare/v1.1.0-dev.7...v1.1.0-dev.8) (2026-08-25)


### Bug Fixes

* add the missing Viki side-panel extension class ([e06d8d8](https://github.com/hashtagbasit/aimal-patches/commit/e06d8d878e9361ce040226b59d8e1820a94d109d))

# [1.1.0-dev.7](https://github.com/hashtagbasit/aimal-patches/compare/v1.1.0-dev.6...v1.1.0-dev.7) (2026-08-25)


### Features

* give the Viki player the whole screen on unfolded foldables ([1188522](https://github.com/hashtagbasit/aimal-patches/commit/1188522023c401ede6f6e7efa9db29b454fd92e7))

# [1.1.0-dev.6](https://github.com/hashtagbasit/aimal-patches/compare/v1.1.0-dev.5...v1.1.0-dev.6) (2026-08-25)


### Features

* add Viki ad removal and HD unlock patches ([9e779e9](https://github.com/hashtagbasit/aimal-patches/commit/9e779e96f456a9d7bc4ffa4a79e8dba376cfe177))

# [1.1.0-dev.5](https://github.com/hashtagbasit/aimal-patches/compare/v1.1.0-dev.4...v1.1.0-dev.5) (2026-08-25)


### Bug Fixes

* drop the initialize hook that stopped subtitles loading ([27f203b](https://github.com/hashtagbasit/aimal-patches/commit/27f203b1bec306fbf37a9dc4e13b207d034d4944))

# [1.1.0-dev.4](https://github.com/hashtagbasit/aimal-patches/compare/v1.1.0-dev.3...v1.1.0-dev.4) (2026-08-25)


### Bug Fixes

* report whether the Crunchyroll subtitle hook is actually present ([74948b6](https://github.com/hashtagbasit/aimal-patches/commit/74948b6db66f8be9434580d6b6edae2971d51d05))

# [1.1.0-dev.3](https://github.com/hashtagbasit/aimal-patches/compare/v1.1.0-dev.2...v1.1.0-dev.3) (2026-08-25)


### Bug Fixes

* make Crunchyroll subtitle settings actually take effect ([9f8683e](https://github.com/hashtagbasit/aimal-patches/commit/9f8683e9913b399e95c8980bc81233516ec538f5))

# [1.1.0-dev.2](https://github.com/hashtagbasit/aimal-patches/compare/v1.1.0-dev.1...v1.1.0-dev.2) (2026-08-25)


### Features

* subtitle styling for Crunchyroll ([7f247cb](https://github.com/hashtagbasit/aimal-patches/commit/7f247cbb0f9e3b9875b4a256c0dc36efa606e825))

# [1.1.0-dev.1](https://github.com/hashtagbasit/aimal-patches/compare/v1.0.2...v1.1.0-dev.1) (2026-08-25)


### Features

* subtitle styling for HBO Max and Disney+ ([6514870](https://github.com/hashtagbasit/aimal-patches/commit/6514870e9b00cb5d0224fe621e0bfbf45452e0ca))

## [1.0.2](https://github.com/hashtagbasit/aimal-patches/compare/v1.0.1...v1.0.2) (2026-08-24)


### Bug Fixes

* make the Crunchyroll aspect toggle appear on 3.117.0 ([672cafe](https://github.com/hashtagbasit/aimal-patches/commit/672cafe273b6ee8d21cdb10b14f80e77521d707f))

## [1.0.1](https://github.com/hashtagbasit/aimal-patches/compare/v1.0.0...v1.0.1) (2026-08-24)


### Bug Fixes

* match Crunchyroll speed table by structure, not moved strings ([04a7d4c](https://github.com/hashtagbasit/aimal-patches/commit/04a7d4c2065c3ce7ca6406f69182afb43fe4bc51))

# 1.0.0 (2026-08-24)


### Bug Fixes

* add the patch list generator the release step calls ([50cf8a8](https://github.com/hashtagbasit/aimal-patches/commit/50cf8a8750b7e4a2a3d09c8a2179bf0a63793293))
* register the generatePatchesList task used by the release ([32be357](https://github.com/hashtagbasit/aimal-patches/commit/32be357cfb2b3438e7234117d6e6d0916ede88ca))
* update readme generator so any-version targets do not crash it ([0ed2c48](https://github.com/hashtagbasit/aimal-patches/commit/0ed2c480c05f045fc8a297d9e7d74f64401ea8df))


### Features

* playback speed and aspect ratio for Crunchyroll, HBO Max and Disney+ ([11ba11d](https://github.com/hashtagbasit/aimal-patches/commit/11ba11d0136d74410a121a68233226d335f7b508))
