import SearchInputComponent from "../components/SearchInputComponent";
import { connect } from "react-redux";
export default connect(mapStateToProps, mapDispatchToProps)(SearchInputComponent);

function mapStateToProps(state){
	return {

	};
}

function mapDispatchToProps(dispatch){
	return {
		onChange: function(companySearchResult){
			console.log("SearchInputContainer , result = >>> " + companySearchResult);
			dispatch({
				type: 'SEARCH/COMPANY_SEARCH_RESULT',
				companySearchResult : companySearchResult
			});
		}
	}
}