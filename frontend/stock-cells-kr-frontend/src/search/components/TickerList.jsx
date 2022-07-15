import React, {Fragment, useState} from 'react';
import { selectTicker } from '../../commons/actions/search.actions';
import { connect } from 'react-redux';
import TickerItem from './TickerItem';


const TickerList = ({searchData, selectTicker}) => {
  return (
	<Fragment>
		{
			searchData != null && 
			searchData.map(
				(data) => (
					<TickerItem key={data.ticker} 
								{...data} 
								onClick={() => selectTicker(data.ticker, data.companyName)}
					/>
				)	
			)
		}
	</Fragment>	
  )
}


// subscribe
// 종목명 검색 API 응답결과를 SUBSCRIBE
// redux의 state 를 props 로 바인딩한다.
const mapStateToProps = (state) => {
	// console.log("state >> ", state);
	return {
		// (NOT)
		// action 에서 전달받는 searchResult을
		// reducer 의 searchData 에 대입해준다.

		// REDUCER 의 searchData 를 props 로 연결해준다.
		searchData : state.searchData.searchResult 
	};
};

// dispatch
// 개별 ticker 선택 이벤트
// 액션 함수에 dispatch 를 연결해준다.
const mapDispatchToProps = (dispatch) => ({
	selectTicker : (ticker, companyName) => dispatch(selectTicker(ticker, companyName))
});

// export default TickerList
export default connect(mapStateToProps, mapDispatchToProps)(TickerList);