import TickerSearchItem from "../../commons/TickerSearchItem";
import {connect} from "react-redux";
export default connect(mapStateToProps, mapDispatchToProps)(TickerSearchItem);

function mapStateToProps(state){

}

function mapDispatchToProps(dispatch){
	return {
		onClick: function(ticker){
			console.log('dispatch >>> ', "{ type = SEARCH/TICKER_CHAGE , ", 'selected_ticker = ' + ticker);
			dispatch({
				type: 'SEARCH/TICKER_CHANGE',
				selected_ticker: ticker
			});
		}
	}
}