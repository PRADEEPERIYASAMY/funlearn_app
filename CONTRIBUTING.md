# Contributing to FunlearnV2

Thank you for taking the time to look at this project. Contributions are welcome — bug fixes, improvements, and new features alike. Please read this before opening a pull request.

## Before You Start

Open an issue first. This is a primarily solo-maintained project, so it helps to align on scope and approach before you invest time writing code. Small, self-contained fixes (typos, minor bugs with obvious fixes) can skip this step.

## How to Contribute

1. Fork the repository and create a branch off `main`.
2. Keep your branch focused on a single concern. Mixed-purpose PRs are harder to review and slower to merge.
3. Match the existing code style — Kotlin conventions, ViewBinding over `findViewById`, Hilt for dependency injection, coroutines over callbacks.
4. Write or update tests where applicable. `FloodFill` and `DataStoreRepository` are the best starting points since they are side-effect-isolated.
5. Run `./gradlew build` and confirm a clean build before opening the PR.
6. Fill out the pull request template completely.

## Reporting Bugs

Use the [bug report template](.github/ISSUE_TEMPLATE/bug_report.md). Include:

- Android version and device (or emulator API level)
- Steps to reproduce
- Expected vs. actual behavior
- Logs or stack traces if available

## Requesting Features

Use the [feature request template](.github/ISSUE_TEMPLATE/feature_request.md). Describe the problem you are solving and why it belongs in this project.

## Security Issues

Do not open a public issue for security vulnerabilities. Follow the process in [SECURITY.md](SECURITY.md).

## Code of Conduct

All interactions in this project are governed by the [Code of Conduct](CODE_OF_CONDUCT.md). Please read it.

## License

By contributing, you agree that your contributions will be licensed under the same [MIT License](LICENSE) that covers this project.
