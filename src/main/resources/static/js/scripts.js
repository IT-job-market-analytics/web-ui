document.addEventListener("DOMContentLoaded", function (event) {

    const ctx = document.getElementById('numberOfVacancy');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: queries,
            datasets: [{
                label: 'Number of vacancies',
                data: vacancyCount,
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    const averageSalary = document.getElementById('averageSalary');
    new Chart(averageSalary, {
        type: 'bar',
        data: {
            labels: queries,
            datasets: [{
                label: 'Average Salary',
                data: averageSalaryData,
                borderWidth: 1,
                backgroundColor: ['rgba(75, 192, 192, 0.2)']
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });


    function getAverageSalary(query) {
        const url = `/analytics/average-salary/${query}`;

        fetch(url)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then((data) => {
                const dates = data.map((item) => item.date)
                const salaries = data.map((item) => item.salary)
                averageSalaryChart(dates, salaries)
            })
            .catch((error) => {
                console.error("Error fetching data:", error);
            });
    }

    let averageSalaryChartInstance;

    function averageSalaryChart(averageSalaryDates, averageSalaryArray) {
        const averageSalaryByQuery = document.getElementById('averageSalaryByQuery');

        if (averageSalaryChartInstance) {
            averageSalaryChartInstance.destroy();
        }

        averageSalaryChartInstance = new Chart(averageSalaryByQuery, {
            type: 'bar',
            data: {
                labels: averageSalaryDates,
                datasets: [{
                    label: 'Average Salary by query',
                    data: averageSalaryArray,
                    borderWidth: 1,
                    backgroundColor: ['rgba(29, 207, 32, 0.2)']
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });

    }


    function getAllAvailable(){
        const url = `/analytics/allAvailable`;

        fetch(url)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then((data) => {
                const selectElement = document.createElement("select");
                selectElement.setAttribute("id", "querySelect");

                const optionElementDef = document.createElement("option");
                optionElementDef.setAttribute("value", "");
                optionElementDef.textContent = "Выбрать...";
                optionElementDef.setAttribute("disabled", "disabled");
                optionElementDef.setAttribute("selected", "selected");
                selectElement.appendChild(optionElementDef);

                data.forEach((item) => {
                    const optionElement = document.createElement("option");
                    optionElement.setAttribute("value", item.query);
                    optionElement.textContent = item.query;
                    selectElement.appendChild(optionElement);
                });

                const container = document.getElementById("allAvailableContainer");
                container.appendChild(selectElement);

                addSelectEventHandler(selectElement);

            })
            .catch((error) => {
                console.error("Error fetching data:", error);
            });
    }

    function addSelectEventHandler(selectElement) {

        selectElement.addEventListener("change", function () {
            const selectedValue = selectElement.value;
            getAverageSalary(selectedValue);
        });
    }

    getAllAvailable();
});

