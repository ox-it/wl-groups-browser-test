# wl-groups-browser-test

This process sits on sole.oucs.ox.ac.uk and logs into WebLearn and writes a line to a csv of whether the course and units groups browser are working properly.

This program logs into WebLearn, goes to Site Info in the Course-Groups-Heroku-Test site, clicks the 'Add Participants' button and makes various check on the groups browser tree,
logs the results to a csv file and emails if there is a problem.

The .csv headings are:

1.   Date and time
2.   Log into WebLearn, navigate to Site Info in the 'Course-Groups-Heroku-Test' site, click the 'Add Participants' button, and does the Course and Unit
Groups tree browsers (top nodes) appear within 10 seconds?
3.   Click on the top node of the Courses tree.  Does 'Ancient History & Classical Archaeology' appears within 10 seconds?
4.   Click on the top node of the Units tree.  Does 'ASUC' appear within 10 seconds?


To develop and test any changes, you can run the java main class from the IDE.

To deploy it to sole, cd to the .class file and run:

jar cfm GroupsBrowserTester.jar ../manifest/Manifest.MF GroupsBrowserTester*.class

then copy the jar to sole (where cron will run it):

scp GroupsBrowserTester.jar root@sole.oucs.ox.ac.uk:/home/ouit0196/groups_browser_test

