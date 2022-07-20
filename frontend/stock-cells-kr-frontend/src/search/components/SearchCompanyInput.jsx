import React, { useState, useEffect } from 'react';
import { searchCompany } from '../../commons/actions/search.actions';
import { connect } from 'react-redux';
import * as Hangul from 'hangul-js';

const SearchCompanyInput = ({dispatch}) => {
	var inputTagText = useState("searchText");
	var searchText = inputTagText[0];
	var setSearchText = inputTagText[1];

	// var searchResultProps = useState("searchResult");
	// var searchResult = searchResultProps[0];
	// var setSearchResult = searchResultProps[1];

	// useEffect(()=>{
	// 	console.log("(useEffect) searchText >>> ", searchText);
	// 	if(searchText){
	// 		fetchSearchTickerAPI(searchText)
	// 	}
	// });

	let fetchSearchTickerAPI = (text) => {
		var disassemble = Hangul.disassemble(text);

		if(disassemble == null || disassemble == undefined) return;
		if(disassemble.length === 0) return;

		var keyword = Hangul.assemble(disassemble);
		keyword = keyword.trim();
		// for(var i=0; i<disassemble.length; i++){
		// 	keyword += disassemble[i];
		// }

		fetch('/ticker/stock?companyName='+keyword)
			.then(function(result){
				return result.json(); 
			})
			.then(function(json){
				console.log("검색어 : ", keyword, " 서버응답 : ", json);
				dispatch(searchCompany(json));
				// setSearchResult({searchResult: json});
			});
	};

	let handleChange = (e) => {
		if (e.isComposing || e.keyCode === 229) {
			return;
		}
		if(e.target.value.trim() === ''){
			setSearchText('종목명 입력');
			return;
		}
		if(e.target.value.length === 0) return;

		// var disassemble = Hangul.disassemble(e.target.value + ' ');
		// var keyword = Hangul.assemble(disassemble);
		// if(disassemble == null || disassemble == undefined) return;
		// if(disassemble === '' || disassemble === ' ') return;
		// if(disassemble.length === 0) return;
		e.preventDefault();
		setSearchText(e.target.value);
		fetchSearchTickerAPI(searchText);
	}

	return (
		<div className="row">
			<div className="col-md-6 m-auto">
				<input 
					type="text" 
					id="search"
					// value={searchText}
					// style={{'wordWrap': 'break-word', 'imeMode': 'active'}}
					style={{whiteSpace: 'pre-wrap', overflowWrap: 'break-word'}}
					className="form-cotrol form-control-lg"
					// https://rrecoder.tistory.com/231
					onInput={function(e){
						if (e.isComposing || e.keyCode === 229) {
							return;
						}
						handleChange(e);
					}.bind(this)}
					/>
			</div>
		</div>
	  );
};

// export default SearchCompanyInput;
export default connect()(SearchCompanyInput);