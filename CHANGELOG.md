# Changelog

## 2.0.1
* Fix lever not replacing in right order
* Fix bad tick scheduling on multi-chunk explosion
* Fix [Issue #8] (https://github.com/RedRelay/ForgeCreeperHeal/issues/8)

## 2.0.0

### news

* Almost all code was rewrite
* Better heal system (should be 100% accurate for unchained explosion)
* Chunk saved data (better RAM consumption)
* Profiler removed
* Better config choice

### bug fixes

* Door respawns in half
* Falling block respawns before their support
* etc ... I'm unable to enumerate all bug fixes, because I have almost rewrite all code ...

## 1.2.0

### news

* Add heal command to instant regenerate terrain

## 1.1.1

### bug fixes

* Concurrent exception from ProfilerRenderEventHandler.onRenderOverlay() fixed
* Adds replacements for illegal arguments from config file

## 1.1.0

### news

* Allows to reload config in game
* Adds [profiler](https://github.com/EyZox/ForgeCreeperHeal/wiki/Profiler)

### bug fixes

* Incompatibily with Glenn Gases mod solved