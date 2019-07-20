function handleResult(resultData)
{	
	console.log("showing data from search");
	
	let example_tbl_element = jQuery("#example_tbl_body");
	
	let rowHTML = "";
	
	rowHTML += "<tr><th>" + resultData["data"] + "</th></tr>";
	
	example_tbl_element.append(rowHTML);
}

function submitForm(formSubmitEvent)
{
	console.log("submitting form data");
	
	formSubmitEvent.preventDefault();
	
	$.post(
		"Example",
		$("#example_form").serialize(),
		(resultData) => handleResult(resultData)
	);
}

$("#example_form").submit((event) => submitForm(event));