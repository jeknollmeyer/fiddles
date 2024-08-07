println '\n-----------  println  -----------\n'
// Full syntax
System.out.println('Hello, World with full syntax');
// Shorthand syntax
println 'Hello World with stripped syntax!\n'
/* 
Block
Comment
*/

println '\n-----------  Variables  -----------\n'
def cat = "meow"
def int one = 1

println cat
println "${cat}"
println	"${cat}".getClass()

println one
println "$one".getClass()



println '\n-----------  Arrays  -----------\n'

def myArray = ["value1", "Value2", "value3", "Value4"]
// Ful syntax
for (int i=0; i < myArray.size(); i++) {
   def greeting = "Loop through an Array and do something with each value: "
   println "${greeting}" + "${myArray[i]}"
   println "${i*10}" + " - Do some arithmatic on array iteration"
   println '${i*10}' + " - Use single quotes to print literal string"
   println 'Use single quotes for regular string, use double quotes to evaluate a Gstrting (variable)'
}
// Shorthand
for (value in myArray) {
   def greeting = "Cleaner syntax to loop through array. value: "
   println "${greeting}" + "$value"
}



println '\n-----------  Ranges  -----------\n'
// Numeric Range
def numbers = 1..10
for (num in numbers) {
   println num  + 'Print the numbers line by line'
}

// Alpha Range
def alphaRange = 'a'..'g'
for (letter in alphaRange) {
   println "$letter" + 'Print the letters, line by line'
}

// Custom Range
def enum days {
	SUN,
	MON,
	TUE,
	WED,
	THU,
	FRI,
	SAT
}

def weekdays = days.MON..days.FRI
for (day in weekdays) {
	println day
}
// Extents of a range (start and end)
println "Extents: "
println weekdays.from
println weekdays.to



println '\n-----------  Functions  -----------\n'

def numberlist = 0..9
for (num in numberlist) {
	//if (isEven(num)) {
	//	println num
	//}
	isEven(num)

}
// divid num by 2 (% is known as a modulous symbol)
def isEven(num) {
	num%2 == 0
	println "${num}"
}



println '\n-----------  Closures  & each / it & findAll -----------\n'
// A closure is simply a block of code
def myClosure = { 
	println 'In a closure'
	println new Date()
}
for (i in 1..3) {
	myClosure()
	sleep(1000)
}

// A closrure is different than a function on how they can be used.
(1..3).each({
	println "In a closure: ${it}"
	})
// Same as:
(1..3).each({ i ->
	println "In a closure: ${i}"
	})

// FindAll
(1..10).findAll({ it%2==0 }).each({ 
	println "In a closrue for even num: ${it}"
	})



println '\n-----------  XML  - XMLSlurper -----------\n'
// XMLSlurper lazy evaluation
// XMLParser eager evaluation - loads whole xml into memory

def file = new File('stl_zoo_sample.gpx')
println file.exists()


def slurper = new XmlSlurper()
def gpx = slurper.parse(file)
println gpx.metadata.name

// For each waypoint element
for (point in gpx.wpt) {
	println point.@lat
	println	point.@lon
}
// Or use .each instead of for loop
gpx.wpt.each {
	println it.@lat
	println	it.@lon
}
