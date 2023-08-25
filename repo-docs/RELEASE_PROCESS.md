# SalesSparrow APIs Release Process

## Introduction

**Objective:** Ensure that our open source backend repository is consistently and effectively updated, with full transparency to our community. Our agile approach, combined with a unique milestone branch system and continuous integration, strives to deliver high-quality updates in a structured manner.

---

## **1. Pre-Release Planning**

### **1.1 Sprint Planning**

- At the beginning of each sprint, prioritize the GitHub issues to be addressed.
- Label issues with milestones corresponding to the anticipated release version (e.g., `v1.2.0`).
- Whenever a bug is discovered or a new feature needs to be implemented, an issue is created on GitHub.
- Issues are tagged appropriately (e.g., `bug`, `enhancement`, `documentation`) and are assigned to the relevant team members.

### **1.2 Continuous Integration (CI)**

- Ensure that any code pushed to the main branch passes through a CI pipeline, which includes lint checks, unit tests, integration tests, and other relevant checks.
- For each issue team members work on, they create a new branch from the `milestone` branch created for that milestone. e.g. `milestone/v1.2.0`
---

## **2. Release Candidate (RC)**

### **2.1 Branching**

- Create a release branch off the main branch named `release/vX.Y.Z` (where `X.Y.Z` is the release version) or use the `milestone` branch approach based on project conventions.
- Developers raise PRs against this branch (or the `milestone` branch if using that approach). PR titles and descriptions should be detailed, linking back to the original issue.

### **2.2 Version Update**

- Update the version in the project configuration or metadata files.

### **2.3 Testing**

- PRs will automatically trigger the CI workflow, ensuring that the combined changes from the entire milestone don't introduce any issues when integrated.
- Ensure that the release candidate is thoroughly tested. This may include:
  - Regression Testing
  - Performance Testing
  - Security Scanning
  - User Acceptance Testing (UAT) for significant features or changes

### **2.4 Documentation**

- Update the `CHANGELOG.md` or equivalent file with the details of the changes, enhancements, fixes, and any potential breaking changes.
- Ensure that all new features, enhancements, or changes are documented appropriately.

---

## **3. Release**

### **3.1 Merging**

- Once the release candidate is deemed stable, merge the `release/vX.Y.Z` branch (or the `milestone` branch) into the `main` branch.
- Create a tagged commit on the main branch with the version number.

### **3.2 GitHub Release**

- On the GitHub repository, navigate to the "Releases" tab.
- Draft a new release, inputting the tag version, release title, and the notes from the `CHANGELOG.md`.
- Publish the release. This action will notify watchers and those who have starred the repository.

### **3.3 Announcements**

- Announce the new release on any relevant communication channels, such as the project's mailing list, Twitter, Discord, or other community platforms.

---

## **4. Post-Release**

### **4.1 Monitoring**

- Monitor any feedback or issues reported by users related to the new release.

### **4.2 Hotfixes**

- If any critical issues or bugs are reported, prioritize and address them immediately.
- For significant bugs, create a hotfix branch off the main branch, apply the fix, and then merge it back into the main branch.
- Tag a new minor release version (e.g., `v1.2.1`) and follow the release steps as mentioned above.

### **4.3 Sprint Retrospective**

- At the end of the sprint or after the release, review the process and any feedback.
- Identify areas of improvement or optimization for the next release cycle.

---

## **5. Best Practices**

- **Transparency:** Ensure all decisions, issues, and discussions related to the release are public and transparent.
- **Communication:** Keep the community informed about release dates, anticipated changes, and any delays.
- **Documentation:** All features and changes should be well-documented to assist users and contributors.
- **Feedback Loop:** Encourage users to report bugs, issues, or provide feedback about the release.

---