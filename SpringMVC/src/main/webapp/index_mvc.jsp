<html>
<body>
<h2>Index MVC Page</h2>


Go to:	<a href="/test" >test page</a>

<br/><br/>

Go to:	<a href="/list" >list page</a>

<br/><br/>
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

<br/><br/>
Go to:	<a href="/springmvc/testCookieValue" >testCookieValue</a>

<br/><br/>


<form action="/springmvc/testPojo" method="post">
    username: <input type="text" name="username">
    <br/>
    password: <input type="password" name="password">
    <br/>
    email: <input type="text" name="email">
    <br/>
    age: <input type="text" name="age">
    <br/>
    city: <input type="text" name="address.city">
    <br/>
    state: <input type="text" name="address.state">
    <br/>
    <input type="submit" value="Submit">
    <br/>

</form>

<br/><br/>

<a href="/springmvc/testServletAPI">test servlet API</a>


<br/><br/>

<a href="/springmvc/testServletAPIwriter">test servlet API writer</a>

<br/><br/>

<a href="/springmvc/testModelAndView">test ModelAndView</a>

<br/><br/>

<a href="/springmvc/testMap">test Map</a>

<br/><br/>

<a href="/springmvc/testSessionAttributes">test Session Attributes</a>

<br/><br/>


<form action="/springmvc/testModelAttribute" method="post">
    <input type="hidden" name="id" value="1" />
    <br/>
    username: <input type="text" name="username" value="Tom">
    <br/>
    email: <input type="text" name="email" value="tom@gmail.com">
    <br/>
    age: <input type="text" name="age" value="12">
    <br/>
    <input type="submit" value="Submit">
    <br/>

</form>

<br/><br/>

<a href="/springmvc/testViewAndViewResolver">test view and view resolver</a>


<br/><br/>

<a href="/springmvc/testView">test view</a>

<br/><br/>

<a href="/springmvc/testRedirect">test redirect</a>
</body>
</html>