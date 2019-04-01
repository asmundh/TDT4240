# Rules of github
- Merges to dev and master must be approved by minimum 1 code reviewer.
- Code is written using branches.
  - One person writing code per branch at once. 
  - Branch naming convention:
    - [feat/hotfix/bug]/[issue]/[name]
    - feat/#24/collision-detection
- Commits are written in English.
  - Issues are referenced in commits
  - Example: "#24 small fix to udp-connection"


## How to push
- git add [files to add to commit]
  - To push all changed files:  git add -A 
  - To push a single file: git add MenuState.java
- git commit -m [commit message]
  - git commit -m "Make changes to MenuState"
- git push
  - To push to certain branch: git push origin [branchname]
