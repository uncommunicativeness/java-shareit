FROM registry.access.redhat.com/ubi8/openjdk-11

ARG APP_HOME=/app
WORKDIR $APP_HOME

ADD target/app.jar $APP_HOME/app.jar

USER root

ARG GID=2000
ARG UID=2001
ARG USER=appuser
ARG GROUP=appgroup
ARG SHELL=/bin/bash
ARG HOME=/home/$USER

RUN mkdir -p $HOME \
  && groupadd -g ${GID} ${GROUP} \
  && useradd --uid ${UID} --gid ${GID} --shell ${SHELL} --home ${HOME} $USER \
  && chown ${UID}:${GID} $HOME \
  && chown -R ${UID}:${GID} $APP_HOME

ENTRYPOINT ["java","-jar","/app/app.jar"]