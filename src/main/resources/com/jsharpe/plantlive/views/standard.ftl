<!DOCTYPE html>
<html>
    <!-- Standard -->
    <head>
        <title>Plantlive</title>
        <style>
            * {
                font-family: Arial, Verdana, sans-serif;
            }

            body {
                background-color: white;
                background-image: linear-gradient(#C6DEA6, #FBFFFE);
                background-repeat: no-repeat;
            }

            .title {
                color: #73877B;
                font-size: 2.5em;
            }

            .no-plant {
                color: #DD0000;
                font-size: 1.2em;
            }

            .input-id {
                margin: 0;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 4px;
                width: 30em;
                font-size: 1em;
            }

            .input-submit {
                margin: 0;
                padding: 11px;
                border: none;
                border-radius: 4px;
                cursor: pointer;

                color: #FBFFFE;
                background-color: #73877B;
                font-size: 1em;
            }

            .input-submit:hover {
                background-color: #7EBDC3;
            }

            .source {
                color: #7EBDC3;
                font-size: 1.2em;
                text-decoration: none;
            }
        </style>
    </head>
    <body>

        <main>

            <h1 class="title">plantlive</h1>

            <#if plantNotFound>
                <!-- No plant -->
                <h2 class="no-plant">Uh oh! There's no such plant!</h2>
            </#if>

            <form action="/" method="GET" accept-charset="UTF-8">
                <input type="text" name="id" placeholder="your plant's id" class="input-id" />
                <input type="submit" value="submit" class="input-submit" />
            </form>

            <br />
            <a href="https://github.com/JohnSharpe/plantlive" class="source" target="_blank">Server source</a>

        </main>

    </body>
</html>