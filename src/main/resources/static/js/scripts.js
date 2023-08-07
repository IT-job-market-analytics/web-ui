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
                console.log(data)
                const dates = data.map((item) => item.date)
                const salaries = data.map((item) => item.salary)
                averageSalaryChart(dates, salaries)
            })
            .catch((error) => {
                console.error("Error fetching data:", error);
            });
    }

    function averageSalaryChart(averageSalaryDates, averageSalaryArray) {
        const averageSalaryByQuery = document.getElementById('averageSalaryByQuery');
        new Chart(averageSalaryByQuery, {
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

    getAverageSalary("Java")
});