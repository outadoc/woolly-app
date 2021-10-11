# woolly-app
This project is a Mastodon client built with Jetpack Compose and Compose Desktop. That means it can run on both Android devices (smartphones, tablets) and desktop (macos, Windows, Linux—anything that can run a JVM).

## Sibling projects
- [mastodonk](https://github.com/outadoc/mastodonk) — A Kotlin/Multiplatform API client for Mastodon, built with Ktor.
- [woolly-server](https://github.com/outadoc/woolly-server) — An authentication proxy for this app, which provides a database of known API keys for each instance and uses them when clients try to sign in.

## What's done, what's left

- [x] Timelines
- [x] HTML parsing of statuses
- [x] Authentication
- [x] Basic navigation
- [x] Status reactions
- [x] Complex navigation (back stack, back button support)
- [x] Status details
- [x] Conversation details
- [x] Composing statuses
- [x] Replying to a status
- [x] Account details
- [x] Account timeline
- [x] Hashtag timeline
- [ ] Direct messaging
- [ ] Timeline streaming
- [ ] Push notifications
- [ ] Nicer icon
- [ ] You name it
