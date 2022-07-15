import TickerSearchItem from "../../commons/components/TickerSearchItem";
import {connect} from "react-redux";
import TickerSearchListItem from "../../commons/TickerSearchListItem";
export default connect(mapStateToProps, mapDispatchToProps)(TickerSearchListItem);

function mapStateToProps(state){
	return {
		companySearchResult: state.companySearchResult
	};
}

function mapDispatchToProps(dispatch){
	return {
		onClick: function(ticker){
			// console.log('dispatch >>> ', "{ type = SEARCH/SELECTED_TICKER , ", 'selected_ticker = ' + ticker);
			dispatch({
				type: 'SEARCH/SELECTED_TICKER',
				selected_ticker: ticker
			});
		}
	}
}