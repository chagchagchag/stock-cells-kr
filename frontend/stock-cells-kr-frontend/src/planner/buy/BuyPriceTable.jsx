import React,{useState, useCallback, useMemo, useRef, useEffect} from 'react'
import { AgGridReact } from 'ag-grid-react';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';
import './styles/styles-ag-grid.css';
import { connect } from 'react-redux';

const BuyPriceTable = ({companyName, ticker}) => {
	const gridRef = useRef();
	const containerStyle = useMemo(() => ({ width: '100%', height: '100%' }), []);
	const gridStyle = useMemo(() => ({ height: '100%', width: '100%' }), []);
	
	const showCnt = 10; // +/- 10개씩 보여주겠다.

	let basePriceInfo = {
		per: null,
		pbr: null,
		eps: null,
		dvr: null,
		marketSum: null,
		price: null,
	};

	let [rowData, setRowData] = useState([
		// {company: '삼성전자', price: 53400, changedPrice: 53400, priceRatio: 100, changedPer: 5, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		// {company: '삼성전자', price: 53400, changedPrice: 53300, priceRatio: 99, changedPer: 4, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		// {company: '삼성전자', price: 53400, changedPrice: 53200, priceRatio: 98, changedPer: 3, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		// {company: '삼성전자', price: 53400, changedPrice: 53100, priceRatio: 97, changedPer: 2, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		// {company: '삼성전자', price: 53400, changedPrice: 53000, priceRatio: 96, changedPer: 1, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
		// {company: '삼성전자', price: 53400, changedPrice: 52900, priceRatio: 95, changedPer: 0, per: 6, eps: 55555, cash: 11111, cashcash: 11111},
	]);

	const generatePriceData = (basePriceInfo) => {
		let array = [];
		for(let i=10; i>0; i--){
			let diff = i;
			let price = basePriceInfo.price;
			let changedPrice = basePriceInfo.price * (1 + 0.01*i);
			let per = basePriceInfo.per;
			let changedPer = per == 0 ? per : changedPrice * per / price;
			// 60000 : 5(per) = 66000 : x
			// 66000 x 5 / 60000
			
			let obj = {
				company: companyName,
				price: price,
				changedPrice: changedPrice.toFixed(0),
				priceRatio: i + "%",
				per: basePriceInfo.per,
				changedPer: changedPer.toFixed(2),
				eps: basePriceInfo.eps
			}

			array.push(obj);
		}

		for(let i=0; i<=10; i++){
			let diff = i;
			let price = basePriceInfo.price;
			let changedPrice = basePriceInfo.price * (1 - 0.01*i);
			let per = basePriceInfo.per;
			let changedPer = per == 0 ? per : changedPrice * per / price;

			let obj = {
				company: companyName,
				price: basePriceInfo.price,
				changedPrice: changedPrice.toFixed(0),
				priceRatio: -1*i + "%",
				per: basePriceInfo.per,
				changedPer: changedPer.toFixed(2),
				eps: basePriceInfo.eps
			}

			array.push(obj);
		}

		setRowData(array);
	}

	useEffect(() => {
		console.log("컴포넌트가 화면에 나타남");
		if(ticker != null && companyName != null){
			fetch('/stock/price?ticker='+ticker)
				.then(function(result){
					return result.json();
				})
				.then(function(json){
					console.log("가격데이터 >>> ", json );
					// basePriceInfo.per = json.per;
					// basePriceInfo.pbr = json.pbr;
					// basePriceInfo.eps = json.eps;
					// basePriceInfo.dvr = json.dvr;
					// basePriceInfo.marketSum = json.marketSum;
					basePriceInfo = {
						...json
					};

					generatePriceData(basePriceInfo);

					console.log("basePriceInfo >>> ", basePriceInfo);
				});
		}

		// if(companyName == null || companyName == undefined || companyName === ''){
		// 	companyName = '';
		// }
		
		return () => { // cleanup 메서드. deps 가 비어있는 경우, 컴포넌트가 사라질때 cleanup 함수가 호출된다.
			console.log("컴포넌트가 화면에서 사라짐");
		}
	}, [companyName || '', ticker]); // deps 에 특정값을 넣으면, 
	// 컴포넌트가 처음 마운트 될때에도 호출되고, 
	// 지정한 값이 바뀔때에도 호출된다. 
	// 그리고 언마운트 시에도 호출되고, 값이 바뀌기 직전에도 호출된다.
	// deps 파라미터가 없을때에는 컴포넌트가 리렌더링 될때마다 호출된다.

	const defaultColDef = useMemo(() => {
	  return {
		editable: true,
		resizable: true,
		minWidth: 100,
		flex: 1,
	  };
	}, []);

	const popupParent = useMemo(() => {
	  return document.body;
	}, []);


	const getCompanyName = () => {
		return companyName;
	};


	const [columnDefs, setColumnDefs] = useState([
		{
			headerName: companyName,
			children:[
				{headerName: '회사명', field: 'company', minWidth: 50, resizable: true},
				{headerName: '조회 가격 (2022/07/08)', minWidth: 120, field: 'price', resizable: true},
				{headerName: '등락율(+/-)', field: 'priceRatio', resizable: true},
				{headerName: '등락율 적용가격', field: 'changedPrice', resizable: true},
				{headerName: '등락율 적용 PER', field: 'changedPer', resizable: true},
			]
		},
		{
			headerName: '실적, 분기 데이터',
			children:[
				{headerName: 'EPS', field: 'eps', resizable: true}
			]
		}
	]);
  
	const onBtnExport = useCallback(() => {
	  gridRef.current.api.exportDataAsCsv();
	}, []);
  
	const onBtnUpdate = useCallback(() => {
	  document.querySelector(
		'#csvResult'
	  ).value = gridRef.current.api.getDataAsCsv();
	}, []);

	const renderHeader = () => {
		if(companyName != null && ticker != null) return companyName + " / " + ticker;
		return "";
	}

	return (
		<div style={containerStyle}>
			<h2>{renderHeader()}</h2>
			<div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
			<div style={{ margin: '10px 0' }}>
				{/* <button onClick={onBtnUpdate}>Show CSV export content text</button> */}
				<button onClick={onBtnExport}>Download CSV export file</button>
			</div>
			<div style={{ flex: '1 1 0', position: 'relative' }}>
				{/* gridContainer 에 height:100% 되게끔 하는 법 찾아볼 것. 100% 먹이면 테이블이 사라짐..... */}
				<div id="gridContainer" style={{height: '1000px', width: '100%'}}>
					<div style={gridStyle} className="ag-theme-alpine">
						<AgGridReact
							ref={gridRef}
							rowData={rowData}
							defaultColDef={defaultColDef}
							suppressExcelExport={true}
							popupParent={popupParent}
							columnDefs={columnDefs}
						></AgGridReact>
					</div>
				</div>
				{/* <textarea id="csvResult">
				Click the Show CSV export content button to view exported CSV here
				</textarea> */}
			</div>
			</div>
		</div>
	)
};

const mapStateToProps = (state) => {
	console.log('subscribe >> (state) = ', state);
	if(state.searchData.selectedCompany == undefined || state.searchData.selectedCompany == null){
		return {
			companyName: null,
			ticker: null
		}
	}
	else{
		if(state.searchData.selectedCompany.companyName == undefined || state.searchData.selectedCompany.companyName == null){
			return {
				companyName: null,
				ticker: null
			}
		}

		return {
			companyName: state.searchData.selectedCompany.companyName,
			ticker: state.searchData.selectedCompany.ticker
		};
	}
	
};

const mapDispatchToProps = (dispatch) => {
	return null;
}

// export default BuyPriceTable
export default connect(mapStateToProps, null)(BuyPriceTable);