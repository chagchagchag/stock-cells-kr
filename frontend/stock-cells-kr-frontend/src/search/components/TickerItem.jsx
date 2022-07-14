import React from 'react'

// ticker. companyName 은 search.reducer.js 내의 "SEARCH/SELECT_TICKER" 을 참고
const TickerItem = ({ticker, companyName, onClick}) =>{
  return (
	<div onClick={onClick}
		className="card card-body mb-1"
		value={ticker}
	>
		<small key={ticker} ticker={ticker}
				onClick={onClick}>
				회사명 : {companyName} / 종목코드 : {ticker}
		</small>
	</div>
  )
}

export default TickerItem;