monthDays = [
  -1 /*0th element not needed*/,
  31,
  28,
  31,
  30,
  31,
  30,
  31,
  31,
  30,
  31,
  30,
  31
]

class Date {
  constructor (day, month, year) {
    this.day = day
    this.month = month
    this.year = year
  }

  static copyOf (date) {
    return new Date(date.day, date.month, date.year)
  }

  getNumOfDaysInMonth (date) {
    if (date.month == 2) {
      if (this.isLeapYear(date)) return 29
    }
    return monthDays[date.month]
  }

  isLeapYear (d) {
    return (d.year & 3) == 0 && (d.year % 100 != 0 || d.year % 400 == 0)
  }

  getNoOfNewYearsTill (newDate) {
    return newDate.year - this.year
  }

  getNoOfMonthsTill (dateTemp) {
    return dateTemp.month - this.month
  }

  getNoOfMonthsTillNewYear () {
    return 12 + 1 - this.month
  }

  getNoOfWeeksTillNewMonth () {
    var daysInThisMonth = this.getNumOfDaysInMonth(this)
    var weekno = parseInt((this.day - 1) / 7)
    var noOfWeeks = parseInt((daysInThisMonth - 1) / 7)
    return noOfWeeks - weekno
  }

  getNoOfWeeksTill (dateEnd) {
    //same month
    var weekno = parseInt((this.day - 1) / 7)
    var weeknoEnd = parseInt((dateEnd.day - 1) / 7)
    return weeknoEnd - weekno
  }

  addYears (numOfYears) {
    this.year += numOfYears
  }

  addMonths (noOfMonths) {
    this.month += noOfMonths
    if (this.month > 12) {
      this.addYears(parseInt(this.month / 12))
      this.month = parseInt(this.month % 12)
    }
  }

  addWeeks (weeks) {
    for (var i = 0; i < weeks; i++) {
      var daysInThisMonth = this.getNumOfDaysInMonth(this)
      if (this.day + 7 <= daysInThisMonth) this.addDays(7)
      else this.addDays(daysInThisMonth - this.day)
    }
  }

  addDays (numOfDays) {
    this.day += numOfDays
    while (this.day > this.getNumOfDaysInMonth(this)) {
      this.day = this.day - this.getNumOfDaysInMonth(this)
      this.addMonths(1)
    }
  }

  equalTo (d2) {
    if (this.year == d2.year && this.month == d2.month && this.day == d2.day)
      return true
    return false
  }

  lessThan (d2) {
    if (this.year < d2.year) return true
    if (this.year == d2.year) {
      if (this.month < d2.month) return true
      if (this.month == d2.month) {
        return this.day < d2.day
      }
    }
    return false
  }

  isValid () {
    if (this.month < 1 || this.month > 12) return false
    if (this.day < 1 || this.day > this.getNumOfDaysInMonth(this)) return false
    if (this.year < 1) return false
    return true
  }
}

class TimePeriod {
  constructor (type, dateStart, dateEnd) {
    this.type = type
    this.dateStart = dateStart
    this.dateEnd = dateEnd
  }

  static compute (type, date, days) {
    var start = Date.copyOf(date)
    var end = Date.copyOf(start)
    switch (type) {
      case 'days':
        end.addDays(days)
        break
      case 'weeks':
        end.addWeeks(days)
        break
      case 'months':
        end.addMonths(days)
        break
      case 'years':
        end.addYears(days)
        break
      default:
        break
    }
    return new TimePeriod(type, start, end)
  }
}

TimePeriod.Type = Object.freeze({
  DAYS: 'days',
  WEEKS: 'weeks',
  MONTHS: 'months',
  YEARS: 'years'
})

function tryAddingYear (timePeriods, dateTemp, dateEnd) {
  if (dateTemp.day == 1 && dateTemp.month == 1) {
    var noOfYears = dateTemp.getNoOfNewYearsTill(dateEnd)
    if (noOfYears > 0) {
      timePeriods.push(
        TimePeriod.compute(TimePeriod.Type.YEARS, dateTemp, noOfYears)
      )
      dateTemp.addYears(noOfYears)
      return true
    }
  }
  return false
}

function tryAddingMonth (timePeriods, dateTemp, dateEnd) {
  if (dateTemp.day == 1) {
    var noOfMonths = 0

    //Check how many months till new year if not same year
    if (dateEnd.year > dateTemp.year) {
      noOfMonths = dateTemp.getNoOfMonthsTillNewYear()
    }
    //Check how many months till required month
    else {
      noOfMonths = dateTemp.getNoOfMonthsTill(dateEnd)
    }

    if (noOfMonths > 0) {
      timePeriods.push(
        TimePeriod.compute(TimePeriod.Type.MONTHS, dateTemp, noOfMonths)
      )
      dateTemp.addMonths(noOfMonths)
      return true
    }
  }
  return false
}

function tryAddingWeek (timePeriods, dateTemp, dateEnd) {
  if ((dateTemp.day - 1) % 7 == 0) {
    var noOfWeeks = 0

    //Check how many weeks till required week
    if (dateEnd.year == dateTemp.year && dateEnd.month == dateTemp.month) {
      noOfWeeks = dateTemp.getNoOfWeeksTill(dateEnd)
    }
    //Check how many weeks till new month
    else {
      noOfWeeks = dateTemp.getNoOfWeeksTillNewMonth()
    }

    if (noOfWeeks > 0) {
      timePeriods.push(
        TimePeriod.compute(TimePeriod.Type.WEEKS, dateTemp, noOfWeeks)
      )
      dateTemp.addWeeks(noOfWeeks)
      return true
    }
  }
  return false
}

function tryAddingDays (timePeriods, dateTemp, dateEnd) {
  //Check how many days till new week
  var noOfDays = 0

  //if its the same week then add only that many days
  if (
    dateEnd.year == dateTemp.year &&
    dateEnd.month == dateTemp.month &&
    parseInt((dateEnd.day - 1) / 7) == parseInt((dateTemp.day - 1) / 7)
  ) {
    noOfDays = dateEnd.day - dateTemp.day
  } else {
    noOfDays = 7 - ((dateEnd.day - 1) % 7)
  }

  if (noOfDays > 0) {
    timePeriods.push(
      TimePeriod.compute(TimePeriod.Type.DAYS, dateTemp, noOfDays)
    )
    dateTemp.addDays(noOfDays)
  }
}

function condense (timePeriods) {
  if (timePeriods.length < 2) return timePeriods
  var timePeriodsCondensed = []
  var prev = timePeriods[0]
  for (var i = 1; i < timePeriods.length; i++) {
    if (prev.type != timePeriods[i].type) {
      timePeriodsCondensed.push(prev)
      prev = timePeriods[i]
    } else {
      prev.end = timePeriods[i].end
    }
  }
  timePeriodsCondensed.push(prev)
  return timePeriodsCondensed
}

exports.Date = Date
exports.solve = (dateStart, dateEnd) => {
  //   console.log(dateStart)
  //   console.log(dateEnd)
  if (!dateEnd.isValid() || !dateStart.isValid()) {
    console.log('Invalid input')
    return []
  }
  if (dateEnd.lessThan(dateStart) || dateStart.equalTo(dateEnd)) {
    console.log('start date beyond end date')
    return []
  }

  dateTemp = Date.copyOf(dateStart)
  timePeriods = []

  while (dateTemp.lessThan(dateEnd)) {
    //check start of year
    if (!tryAddingYear(timePeriods, dateTemp, dateEnd)) {
      if (!tryAddingMonth(timePeriods, dateTemp, dateEnd)) {
        if (!tryAddingWeek(timePeriods, dateTemp, dateEnd)) {
          tryAddingDays(timePeriods, dateTemp, dateEnd)
        }
      }
    }
  }
  return condense(timePeriods)
}
