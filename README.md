# teal-duck

## Importing project to eclipse
1. clone with git with ```git clone https://github.com/teal-duck/teal-duck.git```
2. install gradle to eclipse 
  1. Help -> Install new software... -> Add... (Name: gradle, Location: http://dist.springsource.com/release/TOOLS/gradle)
  2. Select only 'Extensions / Gradle Integration' NOT 'Uncategorized'
  3. Press Finish
3. Import project to eclipse via gradle (https://github.com/libgdx/libgdx/wiki/Gradle-and-Eclipse)
  1. File -> Import -> Gradle -> Gradle Project -> Browse for teal-duck folder (root of project) -> Build Model.
  2. Select all -> Finish.
4. You have now installed teal-duck to eclipse.


## Get changes from GitHub
1. Exit eclipse.
2. Run ```git pull``` in teal-duck
3. Re-open eclipse.


## Push changes to GitHub
1. Exit eclipse.
2. Run ```git status``` to see all changes you have made to project.
3. Run ```git add <filename>``` for each file added/edited.
4. Run ```git commit -m "<commit message>"``` Try to add a helpful message describing your change(s).
5. Run ```git push``` to push changes to GitHub.


## Add a dependency
1. Find package on http://search.maven.org/
2. Open teal-duck/build.gradle
3. Add line ```compile "<group>:<name>:<version>"``` within ```dependencies {...}``` for relevant project (core or desktop)
