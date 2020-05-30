var timeline = require('./timeline');
var Date = require('./timeline').Date

date1 = new Date(1,4,2012)
date2 = new Date(2,4,2012)
console.log(timeline.solve(date1,date2))