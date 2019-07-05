<!DOCTYPE html>
<html>
    <!-- Standard -->
    <head>
        <title>Plantlive</title>
    </head>
    <body>
        <h1>Welcome to Plantlive</h1>
        <#if plantNotFound>
            <!-- No plant -->
            <h2>Uh oh! There's no such plant!</h2>
        </#if>
        <form action="/" method="GET" accept-charset="UTF-8">
            <input type="text" name="id" placeholder="Please enter your UUID" />
            <input type="submit" />
        </form>
    </body>
</html>