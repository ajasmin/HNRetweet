HNRetweet
---------

HNRetweet is an userscript that adds *retweet* links to the [Hacker News][1] site.

![Screenshot 1](https://github.com/ajasmin/HNRetweet/raw/master/media/hn1.png)

Clicking on a *retweet* link for a story lets you retweet that story from [@HNTweets][2].  
If a low-profile story wasn't tweeted by @HNTweets the script lets you send a tweet in the same format.

![Screenshot 2](https://github.com/ajasmin/HNRetweet/raw/master/media/hn3.png)

**Installing:**

- In Firefox install [Greasemonkey][3] and click on the install button bellow.
- In Chrome just click on the install button bellow.
- In Opera right-click on the button bellow select *"Save Linked Content As..."* and put the script in an empty folder  
Then set that folder as the userscripts folder in:  
[Opera button] > [Settings] > [Preferences] / [Advanced] / [Content] / [JavaScript options]

[
![Install](https://github.com/ajasmin/HNRetweet/raw/master/media/install.png)
][4]

*Note: I'm not sure about other browsers. Let me know if it works for you...*

**Design:**

A [Compojure][5] web app running on [App Engine][6] receives new tweets from @HNTweets and maintains a list of backlinks from each HN post to the corresponding tweet.

The user script adds *retweet* links to each post containing the post id from the comment link or the post URL if there isn't a comment link. These links refer to an (hn-retweet.appspot.com/retweet/...) URL for the webapp that redirect to the coresponding [retweet intent][7].


  [1]: http://news.ycombinator.com
  [2]: https://twitter.com/HNTweets
  [3]: https://addons.mozilla.org/en-US/firefox/addon/greasemonkey/
  [4]: http://userscripts.org/scripts/source/125382.user.js
  [5]: https://github.com/weavejester/compojure
  [6]: http://code.google.com/appengine/
  [7]: https://dev.twitter.com/docs/intents#retweet-intent