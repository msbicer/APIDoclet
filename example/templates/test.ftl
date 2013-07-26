<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="content-type" content="text/html;charset=UTF-8" />
  <title>API Doclet Test</title>
  <style type="text/css">
	@page { 
		size: A4 portrait;
		margin: 0.5in;
		border: none;
		padding: 1em;
		background: #fff url('${resources!""}logo.png') no-repeat right top;
		background-size:202px 73px;
		@top-center { content: element(header, last-except) }
		@bottom-center { content: element(footer, last-except) }
	}
	
	.newpage {
		page-break-before: always;
	}
	
	.firstpage {
		page-break-before: avoid;
	}
	
	.table {
		border-spacing:0px;
		border-collapse:collapse;
		page-break-inside:avoid;
		page-break-after:auto;
		page-break-before:auto;
	}
	
	.table td {
		border: 1px solid black;
		padding: 2px;
	}
	
	*{font-family:'Bitstream Vera Sans'}
	
	div,span,td{
		font: 12px 'Bitstream Vera Sans';
	}
	
	.table .header {
		background-color: #cccccc;
		font: 13px 'Bitstream Vera Sans';
		font-weight:bold;
	}
	
	.title {
		font: 15px 'Bitstream Vera Sans';
		font-weight:bold;
	}
	
	pre {font-size:10px;overflow:auto;width:5.5in;}
	
	ul,li{margin-top:2px;margin-bottom:2px}
	
	.paragraph {margin-bottom:30px}
	
	#header {
	    display: block;
	    position: running(header);
	}
	#footer {
		display: block;
		position: running(footer);
	}
	
	.toc-entry { 
	    prototype-insert-position: current;
	    font-size: 14pt 
	}
	.chapter { make-element: toc-entry content }
  </style>
  <#include "lib/macros.ftl">
</head>
<body>
  <div id="header"></div>
  <div id="footer"></div>
  <div class="newpage firstpage">
  	<table style="width:100%;height:100%">
  	<tr><td style="vertical-align:middle" valign="center" align="center">
  		<h1>TEST API Document</h1>
  	</td></tr>
  	</table>
  </div>
  <div class="toc newpage">
	  <h1>Table of Contents</h1>
	  <#list classes as klazz>
	  	<li><a href="#class-${klazz.name}">${klazz.module!klazz.name}</a></li>
	  	<#list klazz.handlers as handler>
	  		<ul><a href="#handler-${handler.name}">${handler.name}</a></ul>
	  	</#list>
	  </#list>
	  
	  <li>
	  <#list models?keys as key>
	  	<#assign model=models[key] />
	  	
	  	<a href="#model-${model.qualifiedName}">${model.name}</a>
	  </#list>
	  </li>
  </div>
  
  <div class="newpage">
  	  <#list classes as klazz>
	  	<h2 class="chapter" id="class-${klazz.name}">${klazz.module!klazz.name}</h2>
	  	<#list klazz.handlers as handler>
	  		<div class="paragraph chapter" id="handler-${handler.name}">
	  			<table class="table" width="100%">
	  				<tr>
	  					<td width="33%" class="header">Function Name</td>
	  					<td>${handler.name}</td>
	  				</tr>
	  				<tr>
	  					<td class="header">Endpoint</td>
	  					<td><@join data=handler.endpoints separator="<br/>" /></td>
	  				</tr>
	  				<tr>
	  					<td class="header">Description</td>
	  					<td>${handler.description}</td>
	  				</tr>
	  				<tr>
	  					<td class="header">Method</td>
	  					<td><@join data=handler.methods separator="<br/>" /></td>
	  				</tr>
	  			</table>
	  			<#if handler.parameters?? && handler.parameters?size&gt;0>
	  			<table class="table" width="100%">
	  				<tr>
	  					<td class="header" colspan="4">Request Parameters</td>
	  				</tr>
	  				<tr>
	  					<td width="25%" class="header">Name</td>
	  					<td width="25%" class="header">Type</td>
	  					<td width="25%" class="header">Required</td>
	  					<td width="25%" class="header">Description</td>
	  				</tr>
	  				<#list handler.parameters as param>
	  				<tr>
	  					<td>${param.name}</td>
	  					<#if models[param.qualifiedType]??>
	  						<td><a href="#model-${param.qualifiedType}">${param.type}</a></td>
	  					<#else>
	  						<td>${param.type}</td>
	  					</#if>
	  					<td><#if param.required>Y<#else>N</#if></td>
	  					<td>${param.description!""}</td>
	  				</tr>
	  				</#list>
	  			</table>
	  			</#if>
	  			<table class="table" width="100%">
	  			<tr>
	  				<td class="header" colspan="2">
	  				Response
	  				</td>
	  			</tr>
	  			<tr>
	  				<td width="33%" class="header">Type</td>
	  				<td class="header">Description</td>
	  			</tr>
	  			<tr>
	  				<#if handler.responseType?? && models[handler.qualifiedResponseType]??>
  						<td><a href="#model-${handler.qualifiedResponseType}">${handler.responseType}</a></td>
  					<#else>
		  				<td>${handler.responseType!""}</td>
  					</#if>
	  				<td>${handler.responseDescription!""}</td>
	  			</tr>
	  			<tr>
	  				<td class="header">Request Example</td>
	  				<td><a href="javascript:void(0)">${handler.requestExample!""}</a></td>
	  			</tr>
	  			<tr>
	  				<td class="header">Response Example</td>
	  				<td><pre>${handler.responseExample!""}</pre></td>
	  			</tr>
	  			</table>
	  		</div>
	  	</#list>
	  </#list>
	  <div style="page-break-after:avoid;page-break-before:avoid"></div>
  </div>
  <div class="newpage">
	  <h1>Models</h1>
	  <#list models?keys as key>
	  	<#assign model=models[key] />
	  	
	  	<h2 class="chapter" id="model-${model.qualifiedName}">${model.name}</h2>
	  	
	  	<#if model.members?? && model.members?size&gt;0>
		<table class="table" width="100%">
			<tr>
				<td width="25%" class="header">Name</td>
				<td width="25%" class="header">Type</td>
				<td width="50%" class="header">Description</td>
			</tr>
			<#list model.members as member>
			<tr>
				<td>${member.name}</td>
				<td>${member.type}</td>
				<td>${member.description!""}</td>
			</tr>
			</#list>
		</table>
		</#if>
	  	
	  </#list>
  </div>
</body>
</html>  