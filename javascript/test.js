var timeline = require('./timeline');
var Date = require('./timeline').Date

date1 = new Date(1,4,2012)
date2 = new Date(5,4,2013)
console.log(timeline.solve(date1,date2))