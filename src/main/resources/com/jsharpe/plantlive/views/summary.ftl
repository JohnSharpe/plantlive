<!DOCTYPE html>
<html>
    <!-- Summary -->
    <head>
        <title>Plantlive</title>
        * {
            font-family: Arial, Verdana, sans-serif;
        }

        body {
            background-color: white;
            background-image: linear-gradient(#C6DEA6, #FBFFFE);
            background-repeat: no-repeat;
        }
    </head>
    <body>

        <main>

            <h1 class="title">plantlive</h1>

            <!-- TODO print latest as a timestamp -->
            <!-- TODO remember it can be null if there are no readings! -->

            <!-- TODO properly express the plant type -->
            <p>Temperature: ${summary.temperature}</p>
            <p>Humidity: ${summary.humidity}</p>
            <p>Light: ${summary.light}</p>
            <p>Conductivity: ${summary.conductivity}</p>

        </main>

    </body>
</html>