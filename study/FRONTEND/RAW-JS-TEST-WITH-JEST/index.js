const sum = (a, b) => {
	// return a + b;
	return a + b;
};

const informPlayer = (age, maxScore) => {
	return {
		age, maxScore
	};
}

const checkIfOddNumber = (num) => {
	return num % 2 == 0 ? false : true;
}

const numberArray = (start, end) => {
	let result = [];
	for(let i = start; i<= end; i++){
		result.push(i)
	}
	return result;
}

module.exports = {
	sum,
	informPlayer,
	checkIfOddNumber,
	numberArray
}