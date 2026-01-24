# AWS SERVICES IMPLEMENTATION REPORT

Date: 01/01/2026

Company: Abstergo Industries

Responsible: Paulo

## Introduction

This report presents the process of implementing tools at Abstergo Industries, carried out by Paulo.
The objective of the project was to identify three AWS services with the purpose of achieving immediate cost reduction.

## Project Description

The tools implementation project was divided into three phases, each with its own specific objectives. The project phases are described below:

### Phase 1:
- AWS Storage Gateway (with Amazon S3 + S3 Glacier)

- Main use: Hybrid storage + backup/archiving to the cloud (without changing your production apps yet).

- Use case:
You keep your systems running on-prem (ERP, POS, inventory, prescription systems), 
but you replace expensive on-prem backup infrastructure (tape libraries, backup servers, secondary storage) by backing up to Amazon S3. 
Older backups and compliance archives (e.g., fiscal documents, logs, scanned documents, audit files) 
automatically move to S3 Glacier tiers for much cheaper long-term storage.

### Phase 2:
- AWS Elastic Disaster Recovery (DRS)

- Main use:
Disaster Recovery with low-cost replication (replace and avoid a secondary datacenter).

- Use case:
Instead of maintaining a full standby environment in another location (or paying for duplicate infrastructure), you replicate critical on-premise servers into AWS continuously.
If your main site fails, you can fail over to AWS and keep stores running.

### Phase 3:
- Amazon RDS (managed relational database)

- Main use: 
Move key databases from self-managed on-prem to a managed database (reduced administration and better utilization).

- Use case: 
Pick one "good candidate" database first - usually a reporting DB, a smaller line-of-business DB, or a Read Only replication for BI/analytics.
Migrate it to Amazon RDS (Postgres or MySQL, for example).
- AWS handles backups, patching automation, monitoring hooks, and high availability options, which reduces operational load and "hidden costs" of running databases on-prem.


## Conclusion

The implementation of tools at Abstergo Industries is expected to result in a reduction of costs, improved management, and greater service availability, 
which will increase the company's efficiency and productivity.
It is recommended to continue using the implemented tools and to seek new technologies that can further improve the company's processes.


## Attachments

- https://aws.amazon.com/storagegateway/
- https://aws.amazon.com/disaster-recovery/
- https://aws.amazon.com/free/database/

Signature of the Project Responsible

Paulo