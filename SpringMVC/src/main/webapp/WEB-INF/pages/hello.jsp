<html>
<body>
<h2>Hello Page</h2>

Go to:	<a href="/index" >index page</a>

<br/><br/>

Go to:	<a href="/springmvc/testRequestMapping" >testRequestMapping</a>

<br/><br/>

<!-- This mapping won't work. Because the following is a get method. Controller defined a post method
Go to:	<a href="/springmvc/testMethod" >testMethod</a>
-->

<form action="/springmvc/testMethod" method="post">

    <input type="submit" value="testMethod"/>
</form>


Go to:	<a href="/springmvc/testParamsAndHeaders?username=shubo&age=22" >testParamsAndHeaders</a>

<br/><br/>

Go to:	<a href="/springmvc/testAntPath/sgsdgs/abc" >testAntPath</a>

<br/><br/>


Go to:	<a href="/springmvc/testPathVariable/1" >test path variable</a>

<br/><br/>


Go to:	<a href="/springmvc/testRest/1" >test rest get</a>

<br/><br/>


<form action="/springmvc/testRest" method="post">

    <input type="submit" value="testRestPost"/>
</form>


<br/><br/>


<form action="/springmvc/testRest/1" method="post">

    <input type="hidden" name="_method" value="DELETE"/>
    <input type="submit" value="testRestDelete"/>
</form>



<br/><br/>


<form action="/springmvc/testRest/1" method="post">

    <input type="hidden" name="_method" value="PUT"/>
    <input type="submit" value="testRestPut"/>
</form>

<br/><br/>
Go to:	<a href="/springmvc/testRequestParam?username=shubo&age=22" >testRequestParam</a>

<br/><br/>

<br/><br/>
Go to:	<a href="/springmvc/testRequestHeader" >testRequestHeader</a>

<br/><br/>

</body>
</html>