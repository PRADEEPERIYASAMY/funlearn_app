# Security Policy

## Supported Versions

FunlearnV2 is under active development. Only the latest commit on `main` is supported.

| Version | Supported |
|---------|-----------|
| main (latest) | Yes |
| Older commits | No |

## Reporting a Vulnerability

**Do not open a public GitHub issue for security vulnerabilities.**

If you discover a security issue — including anything related to Firebase rules, authentication bypass, data exposure, or dependency vulnerabilities — please report it privately:

1. Go to the [Security tab](../../security) of this repository and use GitHub's private vulnerability reporting if enabled, or
2. Contact the maintainer directly via GitHub ([@PRADEEPERIYASAMY](https://github.com/PRADEEPERIYASAMY)).

Please include:

- A clear description of the vulnerability and its potential impact.
- Steps to reproduce or a proof-of-concept (if safe to share).
- Any suggested fix or mitigation, if you have one.

## Response Process

- You will receive an acknowledgment within 7 days.
- The maintainer will investigate and, if confirmed, work on a fix.
- Once a fix is available, the issue will be publicly disclosed with appropriate credit to the reporter (unless you prefer to remain anonymous).

## Known Scope

Areas of particular relevance to security in this project:

- **Firebase Security Rules** — Firestore and Realtime Database rules govern data access. Any rule misconfiguration that allows unauthorized reads or writes is a valid report.
- **Authentication flows** — `PhoneVerificationFragment`, `ParentVerificationFragment`, and the role-gating logic in `AuthenticationActivity`.
- **google-services.json** — The file committed to this repository is a placeholder for development. Do not use it in production without replacing it with your own Firebase project credentials.
- **Third-party dependencies** — Outdated libraries with known CVEs are worth reporting if they are exploitable in this project's context.

## Disclaimer

This project is provided as-is for learning and demonstration purposes. Use it in production at your own risk. The maintainer makes no guarantees about the security of any Firebase configuration derived from this codebase.
