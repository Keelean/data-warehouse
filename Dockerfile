FROM sha256:d9fe944835b8e2f8590b49c82ebef58511fe093f10ef53f0e1c774ed89ebdfc9
ARG REPO_PASSWORD=unknown
ARG REPO_USERNAME=unknown
LABEL maintainer="amaezekingsley@ymail.com"
LABEL base.name="Loan Management API"
RUN groupadd -r loanapp && useradd -r -g loanapp loanapp
RUN rm -rf /etc/localtime
RUN ln -s /usr/share/zoneinfo/Africa/Lagos /etc/localtime
RUN locale-gen en_US.UTF-8
RUN en_NG.UTF-8
RUN LANGUAGE en_US:en
ENV REPO_PASSWORD="${REPO_PASSWORD}"
#ADD https://bitbucket.org/izikenmichael/loan-management-system/commits /dev/null
RUN git clone -b feature/backend https://"${REPO_USERNAME}":"${REPO_PASSWORD}"@bitbucket.org/izikenmichael/loan-management-system.git  /dev/null
# RUN git clone -b feature/backend https://keelean:REPO_PASSWORD@bitbucket.org/izikenmichael/loan-management-system.git
WORKDIR /loan-management-system
RUN chmod a+x /loan-management-system && chown loanapp:loanapp /loan-management-system
USER loanapp:loanapp
HEALTHCHECK --interval=10s --retries=5 --start-period=60s --timeout=30s CMD [ "curl -f localhost:8080 || exit 1" ]
ENTRYPOINT ["mvn","spring-boot:run"]