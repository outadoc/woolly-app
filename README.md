# woolly-app
This project is a Mastodon client built with Jetpack Compose and Compose Desktop. That means it can run on both Android devices (smartphones, tablets) and desktop (macos, Windows, Linux—anything that can run a JVM).

## Sibling projects
- [mastodonk](https://github.com/outadoc/mastodonk) — A Kotlin/Multiplatform API client for Mastodon, built with Ktor.
- [woolly-server](https://github.com/outadoc/woolly-server) — An authentication proxy for this app; which provides a database of known API keys for each instance, and uses them when clients try to sign in.

## What's done, what's left

- [x] Timelines
- [x] HTML parsing of statuses
- [x] Authentication
- [x] Basic navigation
- [ ] Nicer icon
- [ ] Status reactions (implementes but broken)
- [ ] Complex navigation (status/account details, back stack, back button support)
- [ ] Composing statuses
- [ ] Timeline streaming
- [ ] Notifications
- [ ] You name it
