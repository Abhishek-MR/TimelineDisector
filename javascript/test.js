var timeline = require('./timeline');
var Date = require('./timeline').Date

date1 = new Date(31,1,2020)
date2 = new Date(31,12,2021)
console.log(timeline.solve(date1,date2))