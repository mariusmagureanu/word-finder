# Welcome to Word Finder

Word Finder is a small java application which searches for any given words in a specified folder.


## How does it work?

Word Finder reads all words from the files in the first level of the specified folder. By the time it finishes reading all of these files it will end up with a dictionary having  the words as keys and a list of file names with word occurrences as values.
Essentially, this dictionary is a union of all words in the first level of folder.

What's a **word**?

Per current implementation, a word is anything that gets matched by the following regex expression: ``[^a-zA-Z]+``. The search is case sensitive, thus ``foo`` will not match ``fOO``.

If any words found, a top ``10`` list will display the filename along with the percentage of its contained words.

Example:

```shell
search $ overview of the good green black compiler
 1. news                          : 85%
 2. book2                         : 85%
 3. world192.txt                  : 71%
 4. paper1                        : 71%
 5. book1                         : 71%
 6. misc.txt                      : 71%
 7. paper2                        : 57%
 8. paper3                        : 57%
 9. bib                           : 57%
10. paper6                        : 42%
```

If no matches are found, a message like the following is displayed:

```shell
search $ ThisDoesNotExist
$ no matches found
```
**Note.** The Word Finder prefers text content, binary or any other content type is not handled, therefore some glitches will pop up!

## How do I run it?

The Word Finder can be run either "bare metal" or within Docker.

* Bare metal
  
  In this scenario ``Java >= 11`` is required.
  
  From within the ``/src`` folder in the repository run the following:

 ```shell
 $ javac com/word/*.java
 $ java com.word.Main /path/to/a/folder
 ```

* Docker
  
  From within the root folder of the repository run the following:

```shell
$ make docker-build
$ make docker-run data=/path/to/a/folder
   ```

Either of the above options will end up with a search prompt.

## How do I quit it?

If entered alone, word finder will treat ``quit`` in a special manner allowing
exiting the running process. Please note that in this situation - casing - is not
accounted for, thus any of the following will exit the application:``quit, Quit, qUiTe..etc ``

However, if ``quit`` is accompanied by other words then it will also be part of the search criteria.

Example:

```shell
search $ quit this
 1. news                          : 100%
 2. misc.txt                      : 100%
 3. paper6                        : 50%
 4. paper4                        : 50%
 5. paper5                        : 50%
 6. world192.txt                  : 50%
 7. progp                         : 50%
 8. paper2                        : 50%
 9. paper3                        : 50%
10. progl                         : 50%
```

## Can I run tests?

Almost, if you have an IDE which will handle the addition of junit5 to the classpath and handle
the actual testsuite execution for you. There's no [mvn | gradle | ant] or such support the enhance this experience.
