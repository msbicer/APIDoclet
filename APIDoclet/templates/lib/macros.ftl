<#macro join data separator=','>
	<#list data as d>${d}<#if d_index&lt;data?size-1>${separator}</#if></#list>
</#macro>