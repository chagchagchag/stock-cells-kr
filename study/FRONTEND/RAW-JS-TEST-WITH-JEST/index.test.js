const {sum, informPlayer, checkIfOddNumber, numberArray} = require('./index');

describe('index.js 파일 테스트 고고 ', () => {
	it(' sum (1,2) 는 3 이어야 한다', () =>{
		expect(sum(1,2)).toBe(3);
	});

	it(' informPlayer(age, maxScore) 테스트 ', () => {
		expect(informPlayer(24, 63)).toEqual({
			age: 24,
			maxScore: 63
		});
	});

	it(' checkIfOddNumber(num) 테스트 ', () => {
		expect(checkIfOddNumber(3)).toBeTruthy();
	});

	it(' numberArray 테스트 ', () => {
		expect(numberArray(1,21)).toContain(11);
	});
});