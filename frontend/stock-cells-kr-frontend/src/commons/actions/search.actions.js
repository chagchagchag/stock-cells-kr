let nextSearchId = 0;

export const searchCompany = (list) => ({
	type: "SEARCH/SEARCH_COMPANY",
	id: nextSearchId++,
	searchResult: list
});

export const selectTicker = (_ticker, _companyName) => ({
	type: "SEARCH/SELECT_TICKER",
	ticker: _ticker,
	companyName: _companyName
});
