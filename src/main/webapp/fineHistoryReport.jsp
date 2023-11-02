<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="./Assets/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <!-- Custom styles for bootstrap template-->
    <link href="./Assets/css/sb-admin-2.min.css" rel="stylesheet">
    <title>Library - Fine History</title>
    <!-- Get Books -->
    <script>
        fetch("http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/users").then(
            res => {
                res.json().then(
                    data => {
                        if (data.length > 0) {
                            var temp = "<option disabled selected>Select a user</option>";
                            data.forEach((itemData) => {
                                temp += "<option value='"+itemData.Id+"'>" + itemData.Name + " - (" + itemData.Email + ")</option>";
                            });
                            document.getElementById('users').innerHTML = temp;
                        }
                    }
                )
            }
        )
    </script>
    <script src="https://code.highcharts.com/highcharts.js"></script>
</head>
<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">
    <jsp:include page="Shared Components/headersidebar.html" />
            <!-- Begin Page Content -->
            <div class="container-fluid">
                <!-- Page Heading -->
                <h1 class="h3 mb-2 text-gray-800">Fine History Report</h1>
                <p class="mb-4">Fill out the form to view fine history for a customer</p>
                <!-- DataTales -->
                <div class="grid-container" style="display: flex; flex-wrap: wrap;">
                    <div class="col-xl-7 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                            <h6 class="m-0 font-weight-bold text-primary">Fine History</h6>
                        </div>
                        <div class="card-body">
                            <form>
                                Select a user from the list and click "view fines" to update chart and table data.
                                <div class="form-group">
                                    <select required name="users" id="users">
                                    </select>
                                </div>
                                <div class="form-group">
                                    <input type="month" id="monthFilter" name="monthFilter">
                                </div>
                                <button id="submitbtn" type="button" style="width: 25%" class="btn btn-primary btn-block">
                                    View Report
                                </button>
                            </form>
                        </div>
                    </div>
                    </div>
                    <div class="col-xl-5 col-lg-6">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Fine Payment Summary</h6>
                            </div>
                            <div class="card-body">
                                <div id="chart"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xl-12 col-lg-12">
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">All Fines</h6>
                            </div>
                            <div class="card-body">
                                <div class="table-responsive">
                                    <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                        <thead>
                                        <tr>
                                            <th>Fine ID</th>
                                            <th>Issue Date</th>
                                            <th>Paid?</th>
                                            <th>Amount</th>
                                            <th>Book</th>
                                        </tr>
                                        </thead>
                                        <tbody id="fine-data">
                                        </tbody>
                                        <tfoot>
                                        <tr>
                                            <th>Fine ID</th>
                                            <th>Issue Date</th>
                                            <th>Paid?</th>
                                            <th>Amount</th>
                                            <th>Book</th>
                                        </tr>
                                        </tfoot>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
            </div>
            <!-- /.container-fluid -->
        </div>
        <!-- End of Main Content -->
    </div>
    <!-- End of Content Wrapper -->
</div>
<!-- End of Page Wrapper -->

<script>
    const chart = Highcharts.chart('chart', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: 0,
            plotShadow: false
        },
        title: null,
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        accessibility: {
            point: {
                valueSuffix: '%'
            }
        },
        plotOptions: {
            pie: {
                dataLabels: {
                    enabled: true,
                    distance: -50,
                    style: {
                        fontWeight: 'bold',
                        color: 'white'
                    }
                },
                startAngle: -90,
                endAngle: 90,
                center: ['50%', '75%'],
                size: '110%'
            }
        },
        series: [{
            type: 'pie',
            size: '320',
            name: 'Loan Count',
            innerSize: '50%',
            data: [
                ['On Time', null],
                ['Late', null]
            ]
        }]
    });
    document.getElementById("submitbtn").addEventListener("click", function() {
        const customerId = document.getElementById("users").value
        const date = document.getElementById("monthFilter").value
        fetch(`http://localhost:8080/MongoDB-Library-1.0-SNAPSHOT/fine-report?id=`+customerId+`&date=`+date).then(
            res => {
                res.json().then(
                    data => {
                        var temp = "";
                        var paidCount = 0;
                        data.forEach((itemData) => {
                            temp += "<tr>";
                            temp += "<td>" + itemData.id + "</td>";
                            temp += "<td>" + itemData.fine_date + "</td>";
                            temp += "<td>" + itemData.paid + "</td>";
                            temp += "<td>£" + itemData.fine_amount + "</td>";
                            temp += "<td>" + itemData.bookData.Title +" ("+itemData.bookData.Author+")"+ "</td>";
                            if (itemData.paid === true) {
                                paidCount++; // Increment the counter if 'returned' is false
                            }
                        });
                        chart.series[0].setData([['Unpaid', data.length-paidCount],['Paid', paidCount]]);
                        document.getElementById('fine-data').innerHTML = temp;
                        $('#dataTable').DataTable();

                    }
                )
            }
        )
    })
</script>

<style>
    .highcharts-figure,
    .highcharts-data-table table {
        min-width: 320px;
        max-width: 500px;
        margin: 1em auto;
    }

    #chart {
        height: 250px;
    }

    .highcharts-data-table table {
        font-family: Verdana, sans-serif;
        border-collapse: collapse;
        border: 1px solid #ebebeb;
        margin: 10px auto;
        text-align: center;
        width: 100%;
        max-width: 500px;
    }

    .highcharts-data-table caption {
        padding: 1em 0;
        font-size: 1.2em;
        color: #555;
    }

    .highcharts-data-table th {
        font-weight: 600;
        padding: 0.5em;
    }

    .highcharts-data-table td,
    .highcharts-data-table th,
    .highcharts-data-table caption {
        padding: 0.5em;
    }

    .highcharts-data-table thead tr,
    .highcharts-data-table tr:nth-child(even) {
        background: #f8f8f8;
    }

    .highcharts-data-table tr:hover {
        background: #f1f7ff;
    }
</style>

<!-- Bootstrap core JavaScript-->
<script src="./Assets/vendor/jquery/jquery.min.js"></script>
<script src="./Assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="./Assets/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="./Assets/js/sb-admin-2.min.js"></script>

<!-- Page level plugins -->
<script src="./Assets/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="./Assets/vendor/datatables/dataTables.bootstrap4.min.js"></script>
</body>
</html>
