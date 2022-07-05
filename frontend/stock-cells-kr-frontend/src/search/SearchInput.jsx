import React, { Component } from 'react';

class SearchInput extends Component {
	state = {
		text: '',
		list: []
	};
	
	componentDidMount(){
		// ..
	}

	renderSearchResult = (list) => {
		if(list.length === 0) return [];
		
		var searchResultEl = [];

		for(var i=0; i<list.length; i++){
			var each = list[i];
			searchResultEl.push(
				<div key={each.corpCode} className="card card-body mb-1">
					<small>회사명 : {each.name} / 종목코드 : {each.corpCode}</small>
				</div>
			)
		}
		return searchResultEl;
	};

	handleInputChange = (e) => {
		if(e.target.value === ''){
			this.setState({
				text: '종목명 입력',
				list: []
			});
			return;
		}

		this.setState({
			text: e.target.value
		});

		fetch('data/dump-stock-list.json')
			.then(function(result){
				return result.json(); 
			})
			.then(function(json){
				console.log(json);
				this.setState({list:json});
			}.bind(this));

	};

	

	render() {
		return (
		<div className="row">
			<div className="col-md-6 m-auto">
			<h3 className="text-center mb-3">
				<i className="fa-solid fa-car-tunnel"></i> STOCK CELLS
			</h3>
			<input 
				type="text" 
				id="search" 
				className="form-control form-control-lg" 
				onChange={this.handleInputChange}
				placeholder="종목 검색"/>
			</div>
        	{/* <div id="match-list"></div> */}
			<div>
				{this.renderSearchResult(this.state.list)}
			</div>
		</div>
		);
	}
}

export default SearchInput;