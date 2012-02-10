// ==UserScript==
// @name           HNRetweet
// @namespace      http://userscripts.org/users/351198
// @description    Add Retweet links to Hacker News
//
//
// @include        http://news.ycombinator.com/*
// ==/UserScript==

Array.filter(
  document.getElementsByTagName('a'),
  function(a) {return a.text && a.text.match(/^(\d+ comments|discuss)$/)}
).forEach(function(a) {
  var m;
  if (a.href && (m = a.href.match(/^http:\/\/news\.ycombinator\.com\/item\?id=(\d+)$/))) {
    var newa = document.createElement('a');
    newa.href = "http://hn-retweet.appspot.com/retweet/" + m[1];
    newa.text = "Retweet";
    newa.onclick = function() {
      return !window.open(this.href, "hnrtw", "width=550,height=450,personalbar=0,toolbar=0,scrollbars=1,resizable=1,left=408,top=159");
    };
    a.parentNode.insertBefore(newa, a);
    a.parentNode.insertBefore(document.createTextNode(" | "), a);
  }
});

